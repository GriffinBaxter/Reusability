package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.exceptions.IllegalMessageContentException;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.view.incoming.MarketplaceConversationMessagePayload;
import org.seng302.view.outgoing.MarketplaceConversationIdPayload;
import org.seng302.view.outgoing.ConversationPayload;
import org.seng302.view.outgoing.MessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for marketplace conversation. This class includes:
 * POST "/home/conversation/{conversationId}" endpoint used for creating a message in a conversation. If no conversation
 *                                            exists, conversation is created and its ID is returned.
 * GET "/home/conversation" endpoint used for retrieving all the conversations related to a given user. An empty array
 *                          is returned if no conversations exist for the user.
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
     * @throws Exception Exception.
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
            conversation = this.createConversation(sender, storedReceiver.get(), storedCard.get());
            this.createMessage(conversation, sender, marketplaceConversationMessagePayload.getContent());
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
                this.createMessage(storedConversation.get(), sender, marketplaceConversationMessagePayload.getContent());
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

    /**
     * Retrieve all conversations related to a given user.
     * If there are no conversations, then an empty array is returned.
     * No user ID is provided in the URL as the JSESSIONID is used to determine which user is requesting their conversations.
     *
     * @param sessionToken The token used to identify the user.
     * @return Array of conversations belonging to the user.
     */
    @GetMapping("/home/conversation")
    public List<ConversationPayload> createMarketplaceConversationMessage(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdOrReceiverId_OrderByCreatedDesc(currentUser.getId(), currentUser.getId());
        logger.info("Conversations retrieved user with ID {}", currentUser.getId());

        return toConversationPayloadList(conversationList);
    }

    /**
     * Retrieve all messages in a given conversation.
     * If there are no messages, then an empty array is returned.
     * No user ID is provided in the URL as the JSESSIONID is used to determine which user is requesting the messages for the conversation.
     *
     * @param sessionToken The token used to identify the user.
     * @return Array of messages belonging to the given conversation.
     */
    @GetMapping("/home/conversation/{conversationId}/messages")
    public List<MessagePayload> getMarketplaceConversationMessages(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer conversationId) {

        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        Optional<Conversation> optionalConversation = marketplaceConversationRepository.findConversationById(conversationId);

        // 404
        if (optionalConversation.isEmpty()) {
            logger.error("Invalid Conversation ID");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid conversation ID");
        }

        Conversation currentConversation = optionalConversation.get();

        // 403
        if (currentConversation.getReceiver().getId() != currentUser.getId() && currentConversation.getInstigator().getId() != currentUser.getId()) {
            logger.error("User does not have permission to perform action.");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The user does not have permission to perform the requested action"
            );
        }

        List<Message> messageList = marketplaceConversationMessageRepository.findAllByConversationId_OrderByCreatedDesc(conversationId);

        return toMessagePayloadList(messageList);
    }

    /**
     * Converts a list of Conversation objects to a list of ConversationPayload objects to be sent to the frontend.
     *
     * @param conversationList A list of Conversation objects.
     * @return A list of ConversationPayload objects to be sent to the frontend.
     */
    public List<ConversationPayload> toConversationPayloadList(List<Conversation> conversationList) {
        List<ConversationPayload> conversationPayloadList = new ArrayList<>();
        for (Conversation conversation: conversationList) {
            conversationPayloadList.add(conversation.toConversationPayload());
        }
        return conversationPayloadList;
    }

    /**
     * Converts a list of Message objects to a list of MessagePayload objects to be sent to the frontend.
     *
     * @param messageList A list of Message objects.
     * @return A list of MessagePayload objects to be sent to the frontend.
     */
    public List<MessagePayload> toMessagePayloadList(List<Message> messageList) {
        List<MessagePayload> messagePayloadList = new ArrayList<>();
        for (Message message: messageList) {
            messagePayloadList.add(message.toMessagePayload());
        }
        return messagePayloadList;
    }

}
