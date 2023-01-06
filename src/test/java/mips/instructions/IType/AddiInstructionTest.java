package mips.instructions.IType;

import mips.memory.MemoryMapper;
import mips.ProgramCounter;
import mips.Registers;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddiInstructionTest {
  private ProgramCounter pc = new ProgramCounter(0x00000000);
  private Registers reg = new Registers();
  private MemoryMapper mem = new MemoryMapper(8);
  private AddiInstruction target;

  @Test
  public void executeAddPositiveNumbers() {
    reg.setRegister(1, 0x00000002);
    target = new AddiInstruction(1, 1, 0x0001);
    target.execute(pc, mem, reg);
    assertEquals(0x00000003, reg.getRegister(1));
    assertEquals(0x00000004, pc.getPC());
  }

  @Test
  public void executeNegativeImmediate() {
    reg.setRegister(1, 0x00000003);
    target = new AddiInstruction(2, 1, 0xFFFF);
    target.execute(pc, mem, reg);
    assertEquals(0x00000002, reg.getRegister(2));
    assertEquals(0x00000004, pc.getPC());
  }

  @Test
  public void executeOverflow() {
    reg.setRegister(1, 0xFFFFFFFF);
    target = new AddiInstruction(1, 1, 0x0001);
    target.execute(pc, mem, reg);
    assertEquals(0x00000000, reg.getRegister(1));
    assertEquals(0x00000004, pc.getPC());
  }

  @Test
  public void toStringFormatted() {
    target = new AddiInstruction(1, 2, 0xFFFF);
    assertEquals("ADDI $at, $v0, 0xFFFF", target.toString());
  }
}
