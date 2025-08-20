package com.zhh.handsome.tileapp.service.impl;

import com.zhh.handsome.tileapp.service.AIService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ChatMemory chatMemory;
    @Override
    public Flux<String> chat(String content) {
        return chatClient.prompt()
                .user(content)
                .advisors(a->{
                    a.param(AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, "chat");
                })
                .stream()
                .content();
    }
}