package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
public class ProductResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    private MockHttpServletResponse response;

    private final String expectedProductJson = "{\"productCode\":\"%s\"," +
                                        "\"name\":\"%s\"," +
                                        "\"description\":\"%s\"," +
                                        "\"manufacturer\":\"%s\"," +
                                        "\"recommendedRetailPrice\":%.1f," +
                                        "\"created\":\"%s\"}";

    private String expectedJson;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Product product;


    @BeforeAll
    public void setup() throws Exception {
        dGAA = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "address",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dGAA.setId(1);
        gAA = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                "testaddress",
                "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gAA.setId(2);
        user = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2021, 1, 1),
                "123456789",
                "1 Example Street",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(3);
        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2021, 1, 1),
                "123456789",
                "1 Example Street",
                "password",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(4);

        business = new Business(
                "name",
                "some text",
                "92 River Lum Road, Lumbridge, Misthalin",
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0))
        );
        business.setId(1);
        business.addAdministrators(user);

        product = new Product(
                "PROD",
                1,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );

        this.mvc = MockMvcBuilders.standaloneSetup(new ProductResource(
                productRepository, businessRepository, userRepository))
                .build();
    }

    @Test
    public void canRetrieveProductsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductCode(), product.getName(),
                        product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                        product.getCreated()) + "]";

        // when
        List<ProductPayload> list = List.of(new ProductPayload(product.getProductCode(), product.getName(),
                                            product.getDescription(), product.getManufacturer(),
                                            product.getRecommendedRetailPrice(), product.getCreated()));
        when(productRepository.findProductsByBusinessId(1)).thenReturn(list);

        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                                .cookie(new Cookie("JSESSIONID", String.valueOf(user.getId()))))
                                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void canRetrieveProductsWhenBusinessExistsWithDgaaCookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductCode(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated()) + "]";

        // when
        List<ProductPayload> list = List.of(new ProductPayload(product.getProductCode(), product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice(), product.getCreated()));
        when(productRepository.findProductsByBusinessId(1)).thenReturn(list);

        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", String.valueOf(dGAA.getId()))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void canRetrieveProductsWhenBusinessExistsWithGaaCookie() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(gAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductCode(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated()) + "]";

        // when
        List<ProductPayload> list = List.of(new ProductPayload(product.getProductCode(), product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice(), product.getCreated()));
        when(productRepository.findProductsByBusinessId(1)).thenReturn(list);

        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", String.valueOf(gAA.getId()))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void cantRetrieveProductsWhenBusinessDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        expectedJson = "";

        // when
        when(businessRepository.findBusinessById(0)).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/businesses/%d/products", 0))
                .cookie(new Cookie("JSESSIONID", String.valueOf(dGAA.getId()))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void cantRetrieveProductsWhenBusinessExistsWithNonExistingIdCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", String.valueOf(0))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void cantRetrieveProductsWhenBusinessExistsWithNonAdminUserCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", String.valueOf(anotherUser.getId()))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    public void cantRetrieveProductsWhenBusinessExistsWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId())))
                    .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

}
