package org.dhv.pbl5server.chat_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.chat_service.entity.Conversation;
import org.dhv.pbl5server.chat_service.repository.ConversationRepository;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class CreateDefaultConversation implements CommandLineRunner {
    private final ConversationRepository repository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() != 0) return;
        try {
            log.info("--------------------- Creating default conversation ---------------------");
            var user = userRepository.findAll().getFirst();
            var company = companyRepository.findAll().getFirst();
            var conversation = Conversation.builder()
                .user(user)
                .company(company)
                .build();
            repository.save(conversation);
            log.info("--------------------- Successfully created default conversation ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default conversation: ", ex);
        }

    }
}
