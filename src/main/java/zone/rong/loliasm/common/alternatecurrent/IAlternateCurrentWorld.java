package zone.rong.loliasm.common.alternatecurrent;

import zone.rong.loliasm.common.alternatecurrent.structure.WireBlock;
import zone.rong.loliasm.common.alternatecurrent.structure.WorldAccess;

public interface IAlternateCurrentWorld {

    WorldAccess getAccess(WireBlock wireBlock);

}
