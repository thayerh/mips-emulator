package controller;

import javafx.scene.control.TableView;
import mips.MappedMemoryUnit;
import mips.Mips;
import mips.Registers;
import mips.ScreenMemory;
import mips.instructions.IType.SwInstruction;
import mips.instructions.Instruction;

public class ExecuteAllThrottled implements Runnable {
  private static final double TARGET_CONSTANT = 1.4;

  private Mips mips;
  private double clockSpeed;
  private Registers reg;
  private int smemStartAddr;
  private int smemEndAddr;
  private int wordSizeLog;

  public ExecuteAllThrottled(Mips mips, double clockSpeed) {
    this.mips = mips;
    this.clockSpeed = clockSpeed;
    reg = mips.getReg();
    MappedMemoryUnit screenMemory =
        mips.getMemory().getMemUnits().stream()
            .filter(mappedMemoryUnit -> mappedMemoryUnit.getMemUnit() instanceof ScreenMemory)
            .toList()
            .get(0);
    smemStartAddr = screenMemory.getStartAddr();
    smemEndAddr = screenMemory.getEndAddr();
    wordSizeLog = mips.getInstrMem().getWordSizeLog();
  }

  @Override
  public void run() {
    double targetInstrPerMillisec = clockSpeed * 1000000 / 1000 * TARGET_CONSTANT;
    long totalInstructionsExecuted = 0;
    long instructionsExecuted = 0;
    int scrollImemCounter = 0;
    Stopwatch executionStopwatch = new Stopwatch();
    Stopwatch throttleStopwatch = new Stopwatch();

    while (MainController.getIsExecuting()) {
      long timeElapsed = throttleStopwatch.getTimeElapsed();
      if ((throttleStopwatch.getTimeElapsed() != 0)
          && (instructionsExecuted <= targetInstrPerMillisec * timeElapsed)) {
        Instruction nextInstruction = mips.executeNextAndReturnInstruction();
        totalInstructionsExecuted++;
        // Rendering logic
        if (nextInstruction instanceof SwInstruction) {
          SwInstruction swInstruction = (SwInstruction) nextInstruction;
          int targetAddr =
              reg.getRegister(swInstruction.getS()) + signExtend(swInstruction.getImmediate());
          // Display rendering condition
          if (smemStartAddr <= targetAddr && targetAddr <= smemEndAddr) {
            VgaDisplayBMPController.renderVGA((targetAddr - smemStartAddr) >> 2);
          }
        }
        instructionsExecuted++;
        totalInstructionsExecuted++;
        scrollImemCounter = (scrollImemCounter + 1) % 100;
        if (scrollImemCounter == 0) {
          // Index calculation is from InstructionMemory.java
          MainController.getInstructionMemoryController()
                  .scrollTo((mips.getPC() & 0xFFFF) >> wordSizeLog);
        }
      } else if (throttleStopwatch.getTimeElapsed() >= 1) {
        throttleStopwatch.reset();
        instructionsExecuted = 0;
      }
    }

    long delta = executionStopwatch.getTimeElapsed() / 1000;
    System.out.println("Time: " + delta + "s");
    System.out.println("Mips instructions executed: " + totalInstructionsExecuted);
    System.out.println(
        "Clock speed (avg): " + totalInstructionsExecuted / 1000000.0 / delta + " MHz");
  }

  /**
   * Taken from mips.instructions.IType. Not importing from there since it's protected there, and
   * also putting it here shaves off some time. Made it a one-liner to shave off time.
   *
   * @param immediate
   * @return sign-extended immediate
   */
  private static int signExtend(int immediate) {
    return (((immediate >> 15) & 0b1) == 0) ? immediate : (immediate | 0xFFFF0000);
  }
}
