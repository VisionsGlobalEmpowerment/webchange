/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.kaldi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KaldiRecognizer {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected KaldiRecognizer(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(KaldiRecognizer obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        VoskJNI.delete_KaldiRecognizer(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public boolean AcceptWaveform(byte[] data) {
    return AcceptWaveform(data, data.length);
  }
  public boolean AcceptWaveform(short[] data, int len) {
    byte[] bdata = new byte[len * 2];
    ByteBuffer.wrap(bdata).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(data, 0, len);
    return AcceptWaveform(bdata, bdata.length);
  }

  public KaldiRecognizer(Model model, float sample_rate) {
    this(VoskJNI.new_KaldiRecognizer__SWIG_0(Model.getCPtr(model), model, sample_rate), true);
  }

  public KaldiRecognizer(Model model, SpkModel spk_model, float sample_rate) {
    this(VoskJNI.new_KaldiRecognizer__SWIG_1(Model.getCPtr(model), model, SpkModel.getCPtr(spk_model), spk_model, sample_rate), true);
  }

  public KaldiRecognizer(Model model, float sample_rate, String grammar) {
    this(VoskJNI.new_KaldiRecognizer__SWIG_2(Model.getCPtr(model), model, sample_rate, grammar), true);
  }

  public boolean AcceptWaveform(byte[] data, int len) {
    return VoskJNI.KaldiRecognizer_AcceptWaveform(swigCPtr, this, data, len);
  }

  public String Result() {
    return VoskJNI.KaldiRecognizer_Result(swigCPtr, this);
  }

  public String PartialResult() {
    return VoskJNI.KaldiRecognizer_PartialResult(swigCPtr, this);
  }

  public String FinalResult() {
    return VoskJNI.KaldiRecognizer_FinalResult(swigCPtr, this);
  }

}
