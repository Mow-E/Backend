package se.mow_e.controllers;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import se.mow_e.models.Mower;
import se.mow_e.repository.MowerRepo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
//        "spring.datasource.url=jdbc:h2:file:./data/test2;AUTO_SERVER=TRUE",
        "storage.image-dir-path=test_data/images/"
})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private final String mowerId = "e193c17a-9c4e-4e3b-b2bc-f7a8a31a42b0";

    private String token;

    @Autowired
    private RequestTokenConfig requestTokenConfig;

    @Autowired
    private MowerRepo mowerRepo;


    @BeforeEach
    public void setToken() throws Exception {
        token = requestTokenConfig.getToken("user", "pass",
                HttpMethod.POST, "/auth/login", HttpStatus.OK.value());

        Mower mower = new Mower(mowerId);

        if (!mowerRepo.exists(Example.of(mower))) {
            mowerRepo.save(mower);
        }
    }

    @Test
    @Order(1)
    public void bindMowerToUser() throws Exception {
        String response = requestTokenConfig.request(token,
                HttpMethod.POST, "/api/user/addMower/" + mowerId, HttpStatus.OK.value());

        Assertions.assertEquals(JsonPath.read(response, "$.status"), "successful");
    }

    @Test
    @Order(2)
    public void getMowerHistory() throws Exception {
        String response = requestTokenConfig.request(token,
                HttpMethod.GET, "/api/mower/history", HttpStatus.OK.value());

        Assertions.assertEquals(response, "[]");
    }

    @Test
    @Order(3)
    public void getImage() throws Exception {

        Mower mowerByMowerId = mowerRepo.findMowerByMowerId(mowerId);
        mowerByMowerId.addImage("hasbulla");
        mowerRepo.save(mowerByMowerId);

        MockHttpServletResponse imageResponse = requestTokenConfig.requestImage(token, "/api/mower/images/hasbulla");

        Assertions.assertTrue(imageResponse.getContentLength() > 0);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageResponse.getContentAsByteArray());
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        inputStream.close();

        Assertions.assertEquals(bufferedImage.getHeight(), 1320);
        Assertions.assertEquals(bufferedImage.getWidth(), 1980);
    }

}
