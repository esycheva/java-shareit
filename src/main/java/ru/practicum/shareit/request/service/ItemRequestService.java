package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.List;

@Service
public interface ItemRequestService {

    ItemRequestDto create(String userId, ItemRequestDto itemRequestDto);

    ItemRequestDto findById(String userId, String requestId);

    List<ItemRequestDto> getItemRequestsByRequestor(String requestorId);

    Collection<ItemRequestDto> findAllItemRequests();

}
