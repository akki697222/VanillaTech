package akki697222.vanillatech.api.common.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FluidHandler implements VTFluidHandler {
    protected final List<Tank> tanks;

    public FluidHandler(int size, int capacity, int maxTransfer) {
        this.tanks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.tanks.add(new Tank(capacity, maxTransfer));
        }
    }

    public FluidHandler(@NotNull List<Tank> tanks) {
        this.tanks = tanks;
    }

    public static final Codec<FluidHandler> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Tank.CODEC.listOf().fieldOf("tanks").forGetter(h -> h.tanks)
            ).apply(instance, FluidHandler::new)
    );

    @Override
    public int getTanks() {
        return tanks.size();
    }

    public List<Tank> getAllTanks() {
        return tanks;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        if (i < 0 || i >= tanks.size()) return FluidStack.EMPTY;
        return tanks.get(i).getFluid();
    }

    public @NotNull Tank getTank(int i) {
        return tanks.get(i);
    }

    @Override
    public int getTankCapacity(int i) {
        if (i < 0 || i >= tanks.size()) return 0;
        return tanks.get(i).getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack stack) {
        if (i < 0 || i >= tanks.size()) return false;
        return tanks.get(i).isFluidValid(stack);
    }

    @Override
    public int fill(@NotNull FluidStack resource, @NotNull FluidAction action) {
        if (resource.isEmpty()) return 0;

        int totalFilled = 0;
        FluidStack remaining = resource.copy();

        for (Tank tank : tanks) {
            if (tank.isFluidValid(remaining)) {
                int space = tank.getCapacity() - tank.getFluid().getAmount();
                int toFill = Math.min(Math.min(remaining.getAmount(), tank.getMaxTransfer()), space);

                if (toFill > 0) {
                    if (action.execute()) {
                        if (tank.getFluid().isEmpty()) {
                            tank.setFluid(remaining.copyWithAmount(toFill));
                        } else {
                            tank.getFluid().grow(toFill);
                        }
                    }
                    totalFilled += toFill;
                    remaining.shrink(toFill);
                    if (remaining.isEmpty()) break;
                }
            }
        }
        return totalFilled;
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack resource, @NotNull FluidAction action) {
        if (resource.isEmpty()) return FluidStack.EMPTY;

        for (Tank tank : tanks) {
            if (!tank.getFluid().isEmpty() && FluidStack.isSameFluid(tank.getFluid(), resource)) {
                int toDrain = Math.min(Math.min(resource.getAmount(), tank.getMaxTransfer()), tank.getFluid().getAmount());

                if (toDrain > 0) {
                    FluidStack drained = tank.getFluid().copyWithAmount(toDrain);
                    if (action.execute()) {
                        tank.getFluid().shrink(toDrain);
                        if (tank.getFluid().isEmpty()) tank.setFluid(FluidStack.EMPTY);
                    }
                    return drained;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
        if (maxDrain <= 0) return FluidStack.EMPTY;

        for (Tank tank : tanks) {
            if (!tank.getFluid().isEmpty()) {
                int toDrain = Math.min(Math.min(maxDrain, tank.getMaxTransfer()), tank.getFluid().getAmount());

                if (toDrain > 0) {
                    FluidStack drained = tank.getFluid().copyWithAmount(toDrain);
                    if (action.execute()) {
                        tank.getFluid().shrink(toDrain);
                        if (tank.getFluid().isEmpty()) tank.setFluid(FluidStack.EMPTY);
                    }
                    return drained;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public void setFluidInTank(int tank, FluidStack stack) {
        if (tank >= 0 && tank < tanks.size()) {
            tanks.get(tank).setFluid(stack);
        }
    }

    public static class FilteredTank extends Tank {
        private final Fluid filter;

        public static final Codec<FilteredTank> FILTERED_CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        FluidStack.CODEC.fieldOf("fluid").forGetter(Tank::getFluid),
                        Codec.INT.fieldOf("capacity").forGetter(Tank::getCapacity),
                        Codec.INT.fieldOf("maxTransfer").forGetter(Tank::getMaxTransfer),
                        BuiltInRegistries.FLUID.byNameCodec().fieldOf("filter").forGetter(t -> t.filter)
                ).apply(instance, (f, c, m, fl) -> {
                    FilteredTank tank = new FilteredTank(c, m, fl);
                    tank.setFluid(f);
                    return tank;
                })
        );

        public FilteredTank(int capacity, int maxTransfer, Fluid filter) {
            super(capacity, maxTransfer);
            this.filter = filter;
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == filter;
        }
    }

    public static class Tank {
        protected FluidStack fluid;
        protected final int capacity;
        protected final int maxTransfer;

        public static final Codec<Tank> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        FluidStack.CODEC.fieldOf("fluid").forGetter(t -> t.fluid),
                        Codec.INT.fieldOf("capacity").forGetter(t -> t.capacity),
                        Codec.INT.fieldOf("maxTransfer").forGetter(t -> t.maxTransfer)
                ).apply(instance, (f, c, m) -> {
                    Tank tank = new Tank(c, m);
                    tank.setFluid(f);
                    return tank;
                })
        );

        public Tank(int capacity, int maxTransfer) {
            this.fluid = FluidStack.EMPTY;
            this.capacity = capacity;
            this.maxTransfer = maxTransfer;
        }

        public boolean isFluidValid(FluidStack stack) {
            return this.fluid.isEmpty() || FluidStack.isSameFluid(this.fluid, stack);
        }

        public FluidStack drain(int amount, IFluidHandler.FluidAction action) {
            int toDrain = Math.min(amount, this.fluid.getAmount());
            FluidStack drained = this.fluid.copyWithAmount(toDrain);
            if (action.execute()) {
                this.fluid.shrink(toDrain);
                if (this.fluid.isEmpty()) this.fluid = FluidStack.EMPTY;
            }
            return drained;
        }

        public void setFluid(FluidStack fluid) { this.fluid = fluid; }
        public FluidStack getFluid() { return fluid; }
        public int getCapacity() { return capacity; }
        public int getMaxTransfer() { return maxTransfer; }
    }
}