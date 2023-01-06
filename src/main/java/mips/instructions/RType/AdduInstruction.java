package mips.instructions.RType;

import mips.memory.MemoryMapper;
import mips.ProgramCounter;
import mips.Registers;

public class AdduInstruction extends RTypeInstruction {
  public AdduInstruction(int d, int s, int t) {
    super(d, s, t);
    this.name = "ADDU";
  }

  @Override
  public void execute(ProgramCounter pc, MemoryMapper mem, Registers reg) {
    reg.setRegister(d, reg.getRegister(s) + reg.getRegister(t));
    pc.incrementPC(4);
  }
}
