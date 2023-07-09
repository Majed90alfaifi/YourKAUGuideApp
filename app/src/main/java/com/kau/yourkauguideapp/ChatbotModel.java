package com.kau.yourkauguideapp;

import android.content.Context;
import android.content.res.AssetManager;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatbotModel {

    private static final int maxSeqLength = 50;
    private static final String UNKNOWN_TOKEN = "<UNK>";

    private AssetManager assetManager;
    private InputStream modelStream;
    private ByteBuffer model;
    private Interpreter interpreter;
    private final int inputSize = 65;
    private final int outputSize = 13;
    private final String[] allWords = {"is؟", "it؟", "ؤهل", "ابغ", "احة؟", "اكاديمية", "كااديمية؟", "الل", "الو", "اهل", "ايه", "تاح", "تسك", "حاسبات؟", "حسم", "حظه؟", "حمد", "خصص", "خطة", "خطط", "خير", "دراسية؟", "درس", "دكتور", "رحب", "رمج", "زهراني؟", "سلم", "شرط", "صبح", "طلب", "علي", "غامدي؟", "فرد", "فضل", "في", "قبول؟", "قدم", "قني", "كلة", "كلية؟", "لحق", "لزم", "ما", "مساء", "معلومات؟", "مكن", "نظمه", "ورن", "لا", "هو", "هي", "وجد", "ورد", "وحض", "ميل"};
    private final String[] tags = {"البرامج الأكاديمية", "التخصصات", "الخطة الدراسية", "الخطة الدراسية تقنية معلومات ", "الخطة الدراسية نظم معلومات ", "المؤهلات المطلوبة","ايميل دكتور حسام", "ايميل دكتور علي الزهراني", "ايميل دكتور وجدي الغامدي", "تحية", "تحية صباحية", "تحية مسائية", "شروط القبول"};
    private final Map<String, Integer> wordToIndex = new HashMap<>();

    public ChatbotModel(Context context) {
        assetManager = context.getAssets();
        try {

            modelStream = assetManager.open("converted_model.tflite");
            long modelSize = modelStream.available();
            model = ByteBuffer.allocateDirect((int) modelSize);
            byte[] buffer = new byte[(int) modelSize];
            modelStream.read(buffer);
            model.put(buffer);
            model.rewind();


            interpreter = new Interpreter(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < allWords.length; i++) {
            wordToIndex.put(allWords[i], i);
        }

    }

    public String getTag(String message) {

        float[][] inputVal = tokenize(message);
        float[][] output = new float[1][outputSize+1];
        interpreter.run(inputVal, output);
        int maxIndex = 0;
        for (int i = 1; i < outputSize; i++) {
            if (output[0][i] > output[0][maxIndex]) {
                maxIndex = i;
            }
        }
        return tags[maxIndex];
    }
        private float[][] tokenize(String message) {
            List<String> tokensList = Arrays.asList(message.split("\\s+"));
            float[][] inputVal = new float[1][maxSeqLength];
            for (int i = 0; i < tokensList.size() && i < maxSeqLength; i++) {
                String token = tokensList.get(i);
                if (wordToIndex.containsKey(token)) {
                    int index = wordToIndex.get(token);
                    inputVal[0][i] = index;
                } else {
                    // handle out-of-vocabulary words
                    inputVal[0][i] = wordToIndex.get(UNKNOWN_TOKEN);
                }
            }
            System.out.println("Tokens list: " + tokensList);
            System.out.println("Input vector: " + Arrays.toString(inputVal[0]));
            return inputVal;
        }
//    private float[][] tokenize(String message) {
//        String[] tokens = message.split(" ");
//        int[] tokenInts = new int[tokens.length];
//        for (int i = 0; i < tokens.length; i++) {
//            Integer index = wordToIndex.get(tokens[i]);
//            tokenInts[i] = (index != null) ? index - 1 : -1;
//        }
//        float[][] inputVal = new float[1][inputSize];
//        for (int i = 0; i < tokenInts.length; i++) {
//            if (tokenInts[i] >= 0) {
//                inputVal[0][tokenInts[i]] = 1.0f;
//
//            }
//        }
//        return inputVal;
//    }
}