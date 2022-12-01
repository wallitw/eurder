package com.switchfully.eurder.services;

import com.switchfully.eurder.api.dtos.CreateOrderDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.domain.Order;
import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.exceptions.ItemDoesNotExistException;
import com.switchfully.eurder.domain.exceptions.UnknownUserException;
import com.switchfully.eurder.domain.repositories.ItemRepository;
import com.switchfully.eurder.domain.repositories.OrderRepository;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.services.mappers.OrderMapper;
import com.switchfully.eurder.services.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserService userService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ItemRepository itemRepository, UserRepository userRepository, UserMapper userMapper, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto, String authorization) {
        CustomerDto customer = userMapper.toDTO((Customer) getCustomerByAuthorization(authorization));

        //Stock validation
        validateStock(createOrderDto.itemGroupList());
        setPrice(createOrderDto.itemGroupList());
        Order order = new Order(calculateTotalPrice(createOrderDto.itemGroupList()), createOrderDto.itemGroupList(), customer);
        return orderMapper.toDTO(orderRepository.createOrder(order));
    }
    private void setPrice(List<ItemGroup> itemGroupList) {
        for (ItemGroup itemGroup : itemGroupList) {
            itemGroup.setPriceAtMoment(itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getPrice());
            }
    }

    private void validateStock(List<ItemGroup> itemGroupList) {
        for (ItemGroup itemGroup : itemGroupList) {
            if (itemGroup.getAmount() > itemRepository.getItemById(itemGroup.getItemId()).orElseThrow(() -> new ItemDoesNotExistException("The item you tried to order does not exist. The order is cancelled.")).getAmountInStock()) {
                itemGroup.setShippingDate(LocalDate.now().plusDays(7));
            }
            else {
                itemGroup.setShippingDate(LocalDate.now().plusDays(1));
            }
        }
    }

    private double calculateTotalPrice(List<ItemGroup> itemGroupList) {
        double totalPrice = 0;
        for (ItemGroup itemGroup : itemGroupList) {
            totalPrice += itemGroup.getAmount() * itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getPrice();
        }
        return totalPrice;
    }

    private User getCustomerByAuthorization(String authorization){
        String decodedToUsernameAndPassword = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
        String userName = decodedToUsernameAndPassword.split(":")[0];
        return userRepository.getUserByUserName(userName).orElseThrow(() -> new UnknownUserException("User not found"));
    }
}
