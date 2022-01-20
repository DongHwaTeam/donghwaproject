package com.liverary.book.springboot;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sound.sampled.*;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
//public class Application {
//    public static void main(String[] args) { //
//        ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
//        try (SpeechClient client = SpeechClient.create()) {
//
//            responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {
//                ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();
//
//                public void onStart(StreamController controller) {
//                }
//
//                public void onResponse(StreamingRecognizeResponse response) {
//                    responses.add(response);
//                }
//
//                public void onComplete() {
//                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFFF");
//                    String record = "";
//
//                    for (StreamingRecognizeResponse response : responses) {
//                        StreamingRecognitionResult result = response.getResultsList().get(0);
//                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                        record += alternative.getTranscript();
//                        System.out.printf("Transcript : %s\n", alternative.getTranscript());
//                    }
//                }
//
//                public void onError(Throwable t) {
//                    System.out.println(t);
//                }
//            };
//
//            ClientStream<StreamingRecognizeRequest> clientStream = client.streamingRecognizeCallable()
//                    .splitCall(responseObserver);
//
//            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
//                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16).setLanguageCode("ko-KR")// 한국어 설정
//                    .setSampleRateHertz(16000).build();
//            StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder()
//                    .setConfig(recognitionConfig).build();
//
//            StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder()
//                    .setStreamingConfig(streamingRecognitionConfig).build(); // The first request in a streaming call
//            // has to be a config
//
//            clientStream.send(request);
//            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed:
//            // true,
//            // bigEndian: false
//            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
//            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, audioFormat); // Set the system information to
//            // read from the microphone audio
//            // stream
//
//            if (!AudioSystem.isLineSupported(targetInfo)) {
//                System.out.println("Microphone not supported");
//                System.exit(0);
//            }
//            // Target data line captures the audio stream the microphone produces.
//            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
//            targetDataLine.open(audioFormat);
//            targetDataLine.start();
//            System.out.println("Start speaking");
//            long startTime = System.currentTimeMillis();
//            // Audio Input Stream
//            AudioInputStream audio = new AudioInputStream(targetDataLine);
//            while (true) {
//                long estimatedTime = System.currentTimeMillis() - startTime;
//                byte[] data = new byte[6400];
//                audio.read(data);
//                if (estimatedTime > 6000) { // 60 seconds 스트리밍 하는 시간설정(기본 60초)
//                    System.out.println("Stop speaking.");
//                    targetDataLine.stop();
//                    targetDataLine.close();
//                    break;
//                }
//                request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
//                clientStream.send(request);
//            }
//        } catch (Exception e) {
//            System.out.println("&&&&&&&&&&&"+e);
//        }
//        responseObserver.onComplete(); //완료 이벤트 발생
//    }
//
//    /** Demonstrates using the Speech API to transcribe an audio file. */
//    public static void main(String... args) throws Exception {
//        // Instantiates a client
//        try (SpeechClient speechClient = SpeechClient.create()) {
//
//            // The path to the audio file to transcribe
//            String gcsUri = "gs://cloud-samples-data/speech/brooklyn_bridge.raw";
//
//            // Builds the sync recognize request
//            RecognitionConfig config =
//                    RecognitionConfig.newBuilder()
//                            .setEncoding(AudioEncoding.LINEAR16)
//                            .setSampleRateHertz(16000)
//                            .setLanguageCode("en-US")
//                            .build();
//            RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
//
//            // Performs speech recognition on the audio file
//            RecognizeResponse response = speechClient.recognize(config, audio);
//            List<SpeechRecognitionResult> results = response.getResultsList();
//
//            for (SpeechRecognitionResult result : results) {
//                // There can be several alternative transcripts for a given chunk of speech. Just use the
//                // first (most likely) one here.
//                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//                System.out.printf("Transcription: %s%n", alternative.getTranscript());
//            }
//        }
//    }
//}