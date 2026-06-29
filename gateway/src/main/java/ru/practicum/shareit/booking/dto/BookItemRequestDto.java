package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	private Long id;

	@NotNull(message = "Дата начала бронирования обязательна")
	private LocalDateTime start;

	@NotNull(message = "Дата окончания бронирования обязательна")
	private LocalDateTime end;
	private Long itemId;
	private Long bookerId;
	private BookingState state;
	private ItemDto item;
	private UserDto booker;
}
