package com.kau.yourkauguideapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class chatbot_TfliteModel {
    private final int inputSize = 56;
    private final int outputSize = 13;
    ByteBuffer byteBuffer;

    public chatbot_TfliteModel(Context context) {
        // Load your model
        try {
            Interpreter tflite = new Interpreter(loadModelFile(context));


            // Create inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, inputSize}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Create outputs for reference.
            TensorBuffer outputFeature0 = TensorBuffer.createFixedSize(new int[]{1, outputSize}, DataType.FLOAT32);

            // Runs model inference and gets result.
            tflite.run(inputFeature0.getBuffer(), outputFeature0.getBuffer().rewind());

            // Releases model resources if no longer used.
            tflite.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private ByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("converted_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
