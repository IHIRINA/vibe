package com.zhh.handsome.tileapp.config;

import com.zhh.handsome.tileapp.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ChatClientConfig {
    @Bean
    public ChatMemory chatMemory(){
       return new InMemoryChatMemory();
    }
    @Bean
    public ChatClient chatClient(OpenAiChatModel ollamaChatModel, ChatMemory chatMemory){
         return ChatClient.builder(ollamaChatModel)
                 .defaultSystem(SystemConstant.system_constant)
                 .defaultAdvisors(new SimpleLoggerAdvisor()
                 ,new MessageChatMemoryAdvisor(chatMemory))
                 .build();
    }
}
