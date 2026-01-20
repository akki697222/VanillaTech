package akki697222.vanillatech.api.common.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class SingleFluidHandler implements VTFluidHandler {
    protected FluidStack fluid;
    protected int capacity;
    protected int maxTransfer;

    public static final Codec<SingleFluidHandler> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidStack.CODEC.fieldOf("fluid").forGetter(h -> h.fluid),
                    Codec.INT.fieldOf("capacity").forGetter(h -> h.capacity),
                    Codec.INT.fieldOf("maxTransfer").forGetter(h -> h.maxTransfer)
            ).apply(instance, SingleFluidHandler::new)
    );

    public SingleFluidHandler(FluidStack fluid, int capacity, int maxTransfer) {
        this.fluid = fluid.copy();
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    public SingleFluidHandler(int capacity, int maxTransfer) {
        this(FluidStack.EMPTY, capacity, maxTransfer);
    }

    public FluidStack getFluid() {
        return fluid.copy();
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxTransfer(int maxTransfer) {
        this.maxTransfer = maxTransfer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    @NotNull
    public FluidStack getFluidInTank(int tank) {
        return fluid.copy();
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluid.isEmpty() || FluidStack.isSameFluid(fluid, stack);
    }

    @Override
    public int fill(FluidStack resource, @NotNull FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(0, resource)) return 0;

        int space = capacity - fluid.getAmount();
        int toFill = Math.min(Math.min(resource.getAmount(), maxTransfer), space);

        if (toFill <= 0) return 0;

        if (action == FluidAction.EXECUTE) {
            if (fluid.isEmpty()) {
                fluid = new FluidStack(resource.getFluid(), toFill);
            } else {
                fluid.grow(toFill);
            }
            onContentsChanged();
        }
        return toFill;
    }

    @Override
    @NotNull
    public FluidStack drain(FluidStack resource, @NotNull FluidAction action) {
        if (resource.isEmpty() || !FluidStack.isSameFluid(fluid, resource) || fluid.isEmpty()) return FluidStack.EMPTY;

        int toDrain = Math.min(Math.min(resource.getAmount(), maxTransfer), fluid.getAmount());
        if (toDrain <= 0) return FluidStack.EMPTY;

        FluidStack drained = new FluidStack(fluid.getFluid(), toDrain);
        if (action == FluidAction.EXECUTE) {
            fluid.shrink(toDrain);
            if (fluid.getAmount() <= 0) fluid = FluidStack.EMPTY;
            onContentsChanged();
        }
        return drained;
    }

    @Override
    @NotNull
    public FluidStack drain(int maxDrain, @NotNull FluidAction action) {
        if (fluid.isEmpty() || maxDrain <= 0) return FluidStack.EMPTY;

        int toDrain = Math.min(Math.min(maxDrain, maxTransfer), fluid.getAmount());
        FluidStack drained = new FluidStack(fluid.getFluid(), toDrain);

        if (action == FluidAction.EXECUTE) {
            fluid.shrink(toDrain);
            if (fluid.getAmount() <= 0) fluid = FluidStack.EMPTY;
            onContentsChanged();
        }
        return drained;
    }

    protected void onContentsChanged() {

    }

    @Override
    public void setFluidInTank(int tank, FluidStack stack) {
        setFluid(stack);
    }
}