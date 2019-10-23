package com.epam.esm.generate;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.GenerateDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * to generate test data
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
@Component
public class RandomDataGenerator {

    private static final Resource wordsResource = new ClassPathResource("words");
    private static final Resource certificatesResource = new ClassPathResource("certificates");
    private static final Resource emailResource = new ClassPathResource("email");
    private static final Resource descriptionResource = new ClassPathResource("description");


    private List<String> words;
    private List<String> certificates;
    private List<String> descriptions;
    private List<String> emails;
    private TagService tagService;
    private OrderService orderService;
    private CertificateService certificateService;
    private UserService userService;
    private Random random = new Random();

    public RandomDataGenerator(TagService tagService, OrderService orderService, CertificateService certificateService,
                               UserService userService) {
        try {
            this.words = Files.readAllLines(wordsResource.getFile().toPath());
            this.certificates = new ArrayList<>(new HashSet<>(Files.readAllLines(certificatesResource.getFile().toPath())));
            this.descriptions = Files.readAllLines(descriptionResource.getFile().toPath());
            this.emails = Files.readAllLines(emailResource.getFile().toPath());
        } catch (IOException e) {
            throw new GenerateDataException();
        }
        this.tagService = tagService;
        this.orderService = orderService;
        this.certificateService = certificateService;
        this.userService = userService;
    }

    public void generate(int tagsQuantity, int certificatesQuantity, int usersQuantity, int ordersQuantity) {
        generateTags(tagsQuantity);
        List<TagDTO> tags = tagService.findAll(new PageAndSortDTO());
        generateCertificates(certificatesQuantity, tags);
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.findAll(new PageAndSortDTO(null, 1, Integer.MAX_VALUE));
        generateUsers(usersQuantity);
        List<UserDTO> userDTOS = userService.findAll(new PageAndSortDTO(null, 1, Integer.MAX_VALUE));
        generateOrders(ordersQuantity, userDTOS, giftCertificateDTOS);

    }

    private void generateTags(int quantity) {
        for (int i = 0; i < quantity; i++) {
            tagService.save(getRandomTag());
        }
    }

    private void generateCertificates(int quantity, List<TagDTO> allTags) {
        for (int i = 0; i < quantity; i++) {
            certificateService.save(getRandomCertificate(allTags));
        }
    }

    private void generateUsers(int quantity) {
        for (int i = 0; i < quantity; i++) {
            userService.save(getRandomUser());
        }
    }

    private void generateOrders(int quantity, List<UserDTO> userDTOS, List<GiftCertificateDTO> allGiftCertificateDTOS) {
        for (int i = 0; i < quantity; i++) {
            orderService.save(getRandomOrder(userDTOS, allGiftCertificateDTOS));
        }
    }

    private TagDTO getRandomTag() {
        return TagDTO.builder()
                .title(getRandomWord())
                .build();
    }

    private String getRandomWord() {
        String word = words.get(random.nextInt(words.size()));
        words.remove(word);
        return word;
    }


    private GiftCertificateDTO getRandomCertificate(List<TagDTO> tags) {
        String name = certificates.get(random.nextInt(certificates.size()));
        certificates.remove(name);
        String description = descriptions.get(random.nextInt(descriptions.size()));

        return new GiftCertificateDTO.Builder()
                .withName(name)
                .withDescription(description)
                .withExpirationDate(generateRandomExpirationDate())
                .withPrice(generateRandomPrice())
                .withTags(generateRandomTagsSet(tags))
                .build();

    }

    private UserDTO getRandomUser() {
        return UserDTO.builder()
                .email(generateEmail())
                .role(generateRole())
                .password("Qwertyui1!")
                .build();
    }



    private OrderDTO getRandomOrder(List<UserDTO> allUsers, List<GiftCertificateDTO> allGiftCertificateDTOS) {
        return OrderDTO.builder()
                .userEmail(getRandomUserEmail(allUsers))
                .giftCertificates(getRandomGiftCetificateSet(allGiftCertificateDTOS))
                .build();
    }

    private String getRandomUserEmail(List<UserDTO> allUsers) {
        UserDTO userDTO = allUsers.get(random.nextInt(allUsers.size()));
        return userDTO.getEmail();
    }

    private List<GiftCertificateDTO> getRandomGiftCetificateSet(List<GiftCertificateDTO> allGiftCertificateDTOS) {
        List<GiftCertificateDTO> certificateDTOSet = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            certificateDTOSet.add(allGiftCertificateDTOS.get(random.nextInt(allGiftCertificateDTOS.size())));
        }
        return certificateDTOSet;
    }

    private String generateEmail() {
        String email = emails.get(random.nextInt(emails.size()));
        emails.remove(email);
        return email;
    }

    private String generateRole() {
        return random.nextBoolean() ? RoleConstant.ROLE_USER : RoleConstant.ROLE_ADMIN;
    }

    private LocalDate generateRandomExpirationDate() {
        LocalDate start = LocalDate.now();
        long days = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.of(2020, 12, 31));
        return start.plusDays(new Random().nextInt((int) days + 1));
    }

    private BigDecimal generateRandomPrice() {
       return new BigDecimal(BigInteger.valueOf(new Random().nextInt(100001)), 2);
    }

    private Set<TagDTO> generateRandomTagsSet(List<TagDTO> allTags) {
        Set<TagDTO> tags = new HashSet<>();
        for (int i = 0; i < random.nextInt(10); i++) {
            TagDTO tag = allTags.get(random.nextInt(allTags.size()));
            tags.add(tag);
        }
        return tags;
    }


}
