package akki697222.vanillatech.api.common.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public interface VTFluidHandler extends IFluidHandler {
    void setFluidInTank(int tank, FluidStack stack);
}
