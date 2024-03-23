package com.example.provence.service;

import com.example.provence.model.Item;
import com.example.provence.model.Order;
import com.example.provence.model.OrderStatus;
import com.example.provence.model.Vacancy;
import com.example.provence.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    public final OrderRepository orderRepository;
    public final CategoryService categoryService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            // Throw a custom exception when the order is not found.
            throw new IllegalStateException("Order not found with ID: " + orderId);
        }
        return orderOptional;
    }

    public Order createOrder(Order order) {
        List<Item> orderItems = new ArrayList<>();
        for(int i=0; i < order.getItems().size(); i++){
            Item item = categoryService.getItemById(order.getItems().get(i).getId());
            orderItems.add(item);
        }
        ZoneId almatyTimeZone = ZoneId.of("Asia/Almaty");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(almatyTimeZone);


        order.setItems(orderItems);
        order.setOrderDate(zonedDateTime.toLocalDateTime());
        order.setStatus(OrderStatus.PENDING);
        log.info(order.getStatus().toString());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            orderRepository.delete(orderOptional.get());
        } else {
            throw new IllegalStateException("Order not found with ID: " + orderId);
        }
    }

    public String beautifyMessage(Order order){
        int sum = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder content = new StringBuilder("<h2>Номер заказа: " + order.getId() + "</h2>");
        content.append("<table><thead><tr><th>  №  </th><th>|  Название продукта  </th><th>|  Цена  </th><th>|  Количество продукта  </th><th>|  Сумма  </th></tr></thead><tbody>");
        int num = 1;
        for (int i = 0; i < order.getItems().size(); i++) {
            int quantity = 0;
            String itemName = order.getItems().get(i).getName();

            // Count occurrences of this item in the remaining items
            for (int j = i; j < order.getItems().size(); j++) {
                if (itemName.equals(order.getItems().get(j).getName())) {
                    quantity++;
                }
            }
            double item_sum = order.getItems().get(i).getPrice() * quantity;

//            content += "<br/>" + num + ") " + itemName + " (" + order.getItems().get(i).getCategory().getName() + ") * " + quantity;
            content.append("""
                    <tr>
                      <td>| \s""").append(num).append("""
                    </td>
                    <td>| \s""").append(itemName).append("""
                    </td>
                    <td>| \s""").append(order.getItems().get(i).getPrice()).append("""
                    </td>
                    <td>| \s""").append(quantity).append("""
                    </td>
                    <td>| \s""").append(item_sum).append("""
                      </td>
                    </tr>
                    """);
            // Skip the next items with the same name
            for (int j = i + 1; j < order.getItems().size(); j++) {
                if (itemName.equals(order.getItems().get(j).getName())) {
                    i++;
                } else {
                    break;
                }
            }
            num++;
            sum += (int) (order.getItems().get(i).getPrice() * quantity);
        }
        String formattedOrderDate = order.getOrderDate().format(formatter);
        content.append("</tbody></table><br/> Имя клиента: ").append(order.getCustomerName()).append("<br/> Номер клиента: ").append(order.getCustomerPhone()).append("<br/> Время заказа: ").append(formattedOrderDate).append("<br/><br/><b> Общая сумма: ").append(sum).append("тг</b>");
        log.info(content.toString());
        return content.toString();
    }
    private final JavaMailSender javaMailSender;


//= "ice_akpekova@mail.ru"
    public void sendEmail(String subject, String text, String fromEmail) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(fromEmail);
        helper.setSubject(subject);
        helper.setText(text, true);

        javaMailSender.send(message);
    }

    public String beautifyVacancy(Vacancy vacancy) {
        return "<h3>Имя: </h3>" + vacancy.getName()
                + "<br/><h3>Телефон номер: </h3>" + vacancy.getPhone()
                + "<br/><h3>О себе(резюме): </h3>" + vacancy.getResume();
    }

    public int generateCode(){
        int min = 10000;
        int max = 99999;
        Random random = new Random();

        int verificationCode = random.nextInt((max - min) + 1) + min;

        System.out.println("Generated Verification Code: " + verificationCode);
        return verificationCode;
    }
}
