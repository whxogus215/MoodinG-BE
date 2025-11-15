package com.likelion.mooding.infrastructure.openai.application;

import com.likelion.mooding.feedback.application.FeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Primary
@Profile("local")
@Service
public class LocalFeedbackChatCompletionService implements FeedbackChatCompletionService {

    @Override
    public Mono<FeedbackCreateResponse> completeChat(final FeedbackCreateRequest request) {
        return Mono.just(new FeedbackCreateResponse(
            "지금 어떤 마음으로 하루를 보내고 계실지 감히 다 알 수는 없지만, 혹시라도 마음 둘 곳 없이 힘든 시간을 홀로 견뎌내고 계시다면 가만히 어깨를 토닥여 드리고 싶습니다. 겉으로는 애써 담담한 척해도, 속으로는 누구에게도 말 못 할 고민과 아픔으로 지쳐있을지도 모르겠습니다. 그동안 혼자서 얼마나 많은 것을 감당하려 애쓰셨나요.\n"
                + "\n"
                + "우리가 겪는 힘듦은 때로 '왜 나에게만 이런 일이 일어날까' 하는 외로움과 절망감을 동반합니다. 하지만 지금 느끼는 그 막막함과 슬픔은 당신이 무언가에 진심이었기에, 혹은 최선을 다해왔기에 느끼는 자연스러운 감정일 수 있습니다. 그러니 잠시 주저앉아 아파해도 괜찮습니다. 애써 강한 척하지 않아도, 그 자체로 당신은 충분히 소중한 존재입니다.\n"
                + "\n"
                + "캄캄한 밤이 지나면 반드시 새벽이 오듯, 지금의 이 어려운 시간도 언젠가는 지나갈 것입니다. 스스로를 너무 다그치지 말고, 지친 마음이 잠시 쉬어갈 수 있도록 따뜻한 위로를 건네주세요. 당신이 다시 일어설 힘을 얻는 그날까지, 보이지 않는 곳에서라도 묵묵히 당신의 편이 되어 응원하겠습니다."));
    }
}
