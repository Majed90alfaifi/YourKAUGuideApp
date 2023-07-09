package com.kau.yourkauguideapp;

import android.content.Context;
import android.content.res.AssetManager;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class tfmodel {


    AssetManager assetManager;
    InputStream modelStream;
    ByteBuffer model;
    Interpreter interpreter;
    final int inputSize = 56;
    final int outputSize = 13;
    private String[] allWords = {"is؟", "it؟", "ؤهل", "ابغ", "احة؟", "اكاديمية", "اكاديمية؟", "الل", "الو", "اهل", "ايه", "تاح", "تسك", "حاسبات؟", "حسم", "حظه؟", "حمد", "خصص", "خطة", "خطط", "خير", "دراسية؟", "درس", "دكتور", "رحب", "رمج", "زهراني؟", "سلم", "شرط", "صبح", "طلب", "علي", "غامدي؟", "فرد", "فضل", "في", "قبول؟", "قدم", "قني", "كلة", "كلية؟", "لحق", "لزم", "ما", "مساء", "معلومات؟", "مكن", "نظم", "نور", "هلا", "هو", "هي", "وجد", "ورد", "وضح", "يمل"};
    private String[] tags = {"البرامج الأكاديمية", "التخصصات", "الخطة الدراسية", "الخطة الدراسية تقنية معلومات ", "الخطة الدراسية نظم معلومات ", "المؤهلات المطلوبة", "ايميل دكتور حسام", "ايميل دكتور علي الزهراني", "ايميل دكتور وجدي الغامدي", "تحية", "تحية صباحية", "تحية مسائية", "شروط القبول"};
    private Map<String, Integer> wordToIndex = new HashMap<>();


    public void ChatsModel(Context context, String message, String sender) {
        assetManager = context.getAssets();


        try {
            modelStream = assetManager.open("converted_model.tflite");

            // Get the size of the model
            long modelSize = modelStream.available();

            // Allocate a buffer of that size
            model = ByteBuffer.allocateDirect((int) modelSize);

            // Read the model from the InputStream into the buffer
            modelStream.read(model.array());

            // Rewind the buffer for use in Interpreter
            model.rewind();

            interpreter = new Interpreter(model);

            // Prepare input data
            float[][] inputVal = new float[1][inputSize];
            // Set input data values

            // Allocate output data buffer
            float[][] outputVal = new float[1][outputSize];

            // Run inference
            interpreter.run(inputVal, outputVal);

            // Use the output data


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < allWords.length; i++) {
            wordToIndex.put(allWords[i], i);
        }

    }

    void Tokenize(String message) {
        // Tokenize the message
        String[] tokens = message.split(" ");

        // Convert tokens into integers
        int[] tokenInts = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            Integer index = wordToIndex.get(tokens[i]);
            tokenInts[i] = index != null ? index : -1; // Use -1 for unknown words
        }

        // Convert integers into one-hot encoded vector
        float[][] inputVal = new float[1][inputSize];
        for (int i = 0; i < tokenInts.length; i++) {
            if (tokenInts[i] >= 0) {
                inputVal[0][tokenInts[i]] = 1.0f;
            }
        }
    }

    void outputTag() {
        // Run inference
        float[][] outputVal = new float[1][outputSize];
        float[][] inputVal = new float[1][inputSize];
        interpreter.run(inputVal, outputVal);

        // Find the tag with the highest probability
        int maxIndex = 0;
        for (int i = 1; i < outputSize; i++) {
            if (outputVal[0][i] > outputVal[0][maxIndex]) {
                maxIndex = i;
            }
        }

        // Convert the index into a tag
        String tag = tags[maxIndex];
    }


}