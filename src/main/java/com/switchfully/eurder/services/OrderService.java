package com.switchfully.eurder.services;

import com.switchfully.eurder.api.dtos.CreateOrderDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.api.dtos.OrderDto;
import com.switchfully.eurder.api.dtos.OrderReportDto;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.ItemGroup;
import com.switchfully.eurder.domain.Order;
import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.exceptions.ItemDoesNotExistException;
import com.switchfully.eurder.domain.exceptions.UnauthorizedException;
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
        Order order = new Order(calculateTotalPrice(createOrderDto.itemGroupList()), createOrderDto.itemGroupList(), customer);
        return orderMapper.toDTO(orderRepository.createOrder(order));
    }

    private void validateStock(List<ItemGroup> itemGroupList) {
        for (ItemGroup itemGroup : itemGroupList) {
            System.out.println(itemGroup.toString());
            System.out.println(itemGroup.getItemId());
            int currentAmountInStock = itemRepository.getItemById(itemGroup.getItemId()).orElseThrow(() -> new ItemDoesNotExistException("The item you tried to order does not exist. The order is cancelled.")).getAmountInStock();
            if (itemGroup.getAmount() > currentAmountInStock) {
                itemGroup.setShippingDate(LocalDate.now().plusDays(7));
            } else {
                itemGroup.setShippingDate(LocalDate.now().plusDays(1));
            }
            itemRepository.getItemById(itemGroup.getItemId()).orElseThrow(() -> new ItemDoesNotExistException("The item you tried to order does not exist. The order is cancelled.")).setAmountInStock(currentAmountInStock - itemGroup.getAmount());
        }
    }

    private double calculateTotalPrice(List<ItemGroup> itemGroupList) {
        double totalPrice = 0;
        for (ItemGroup itemGroup : itemGroupList) {
            totalPrice += itemGroup.getAmount() * itemRepository.getItemById(itemGroup.getItemId()).orElseThrow().getPrice();
        }
        return totalPrice;
    }

    private User getCustomerByAuthorization(String authorization) {
        String decodedToUsernameAndPassword = new String(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
        String userName = decodedToUsernameAndPassword.split(":")[0];
        return userRepository.getUserByUserName(userName).orElseThrow(() -> new UnknownUserException("User not found"));
    }

    public OrderReportDto getCustomerOrders(String authorization, String userName) {
        CustomerDto customer = userMapper.toDTO((Customer) getCustomerByAuthorization(authorization));
        if (!customer.userName().equals(userName)) {
            throw new UnauthorizedException();
        }
        List<OrderDto> listOfCustomerOrders = orderMapper.toDTO(orderRepository.getCustomerOrders(customer));
        double totalPriceOfAllOrders = listOfCustomerOrders.stream().mapToDouble(orderDto -> orderDto.totalPrice()).sum();

         return new OrderReportDto(totalPriceOfAllOrders, listOfCustomerOrders);
    }
}
