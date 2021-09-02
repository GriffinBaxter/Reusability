package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.exceptions.IllegalMessageContentException;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.view.incoming.MarketplaceConversationMessagePayload;
import org.seng302.view.outgoing.MarketplaceConversationIdPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

/**
 * Controller class for marketplace conversation. This class includes:
 * POST "/home/conversation/{conversationId}" endpoint used for creating a message in a conversation. If no conversation
 *                                            exists, conversation is created and its ID is returned.
 */
@RestController
public class MarketplaceConversationResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    private MarketplaceConversationRepository marketplaceConversationRepository;

    @Autowired
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    /**
     * MarketplaceConversationResource constructor
     * @param userRepository The user repository
     * @param marketplaceCardRepository The marketplace card repository
     * @param marketplaceConversationRepository The marketplace conversation repository
     * @param marketplaceConversationMessageRepository The marketplace conversation message repository
     */
    public MarketplaceConversationResource(UserRepository userRepository,
                                           MarketplaceCardRepository marketplaceCardRepository,
                                           MarketplaceConversationRepository marketplaceConversationRepository,
                                           MarketplaceConversationMessageRepository marketplaceConversationMessageRepository
    ) {
        this.userRepository = userRepository;
        this.marketplaceCardRepository = marketplaceCardRepository;
        this.marketplaceConversationRepository = marketplaceConversationRepository;
        this.marketplaceConversationMessageRepository = marketplaceConversationMessageRepository;
    }

    private static final Logger logger = LogManager.getLogger(MarketplaceConversationResource.class.getName());
    private Conversation conversation;

    /**
     * Create a message in a marketplace conversation.
     * If the conversation does not exist, a conversation is created first.
     *
     * @param sessionToken The token used to identify the user.
     * @param conversationId The ID of the conversation that the message is being added to. Null if new conversation.
     * @param marketplaceConversationMessagePayload contains new message info.
     * @return marketplace conversation ID.
     */
    @PostMapping({"/home/conversation/{conversationId}", "/home/conversation"})
    public ResponseEntity<MarketplaceConversationIdPayload> createMarketplaceConversationMessage(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestBody MarketplaceConversationMessagePayload marketplaceConversationMessagePayload,
            @PathVariable(required = false) Integer conversationId
    ) {

        //401
        User sender = Authorization.getUserVerifySession(sessionToken, userRepository);

        //400
        // check if receiver and marketplace card exists

        Integer receiverId = marketplaceConversationMessagePayload.getReceiverId();
        Optional<User> storedReceiver = userRepository.findById(receiverId);

        if (storedReceiver.isEmpty()) {
            logger.error("Invalid Conversation - invalid receiver id");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Conversation - invalid receiver id"
            );
        }

        Integer cardId = marketplaceConversationMessagePayload.getMarketplaceCardId();
        Optional<MarketplaceCard> storedCard = marketplaceCardRepository.findById(cardId);

        if (storedCard.isEmpty()) {
            logger.error("Invalid Conversation - invalid card id ");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Conversation - invalid card id"
            );
        }


        // if conversationId is null, then create conversation
        if (conversationId == null) {

            //201
            conversation = createConversation(sender, storedReceiver.get(), storedCard.get());
            createMessage(conversation, sender, marketplaceConversationMessagePayload.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(new MarketplaceConversationIdPayload(conversation.getId()));

        } else {

            // check if conversation exists if conversationId is not null
            Optional<Conversation> storedConversation = marketplaceConversationRepository.findConversationById(conversationId);
            if (storedConversation.isEmpty()) {
                //406
                logger.error("Invalid Conversation - conversation id does not exist.");
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Conversation - conversation id does not exist"
                );
            } else {
                //201
                createMessage(storedConversation.get(), sender, marketplaceConversationMessagePayload.getContent());
                return ResponseEntity.status(HttpStatus.CREATED).body(new MarketplaceConversationIdPayload(storedConversation.get().getId()));
            }
        }
    }

    /**
     * Creates a conversation.
     * @param sender The user sending the message.
     * @param receiver The user receiving the message.
     * @param card The card that the sender is messaging about.
     * @return conversation created.
     */
    private Conversation createConversation(User sender, User receiver, MarketplaceCard card) {
        try {
            conversation = new Conversation(sender, receiver, card);
            marketplaceConversationRepository.save(conversation);
            logger.info("Successful Conversation Creation - {}", conversation);
            return conversation;
        } catch (IllegalArgumentException e) {
            //400
            logger.error("Conversation Creation Failure - {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    /**
     * Creates a message.
     * @param conversation The conversation that the message relates to.
     * @param sender The user sending the message.
     * @param content The message content.
     */
    private void createMessage(Conversation conversation, User sender, String content) {
        try {
            Message message = new Message(conversation, sender, content);
            marketplaceConversationMessageRepository.save(message);
            logger.info("Successful Message Creation - {}", message);
        } catch (IllegalArgumentException | IllegalMessageContentException e) {
            //400
            logger.error("Message Creation Failure - {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

}
