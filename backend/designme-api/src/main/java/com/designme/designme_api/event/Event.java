package com.designme.designme_api.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

	public static final String DEFAULT_COLOR = "#3B82F6";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(name = "is_all_day", nullable = false)
	private boolean allDay;

	private LocalTime startTime;

	private LocalTime endTime;

	@Column(nullable = false, length = 7)
	private String color;

	@Column(columnDefinition = "TEXT")
	private String memo;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	private Event(String title, LocalDate startDate, LocalDate endDate, boolean allDay,
			LocalTime startTime, LocalTime endTime, String color, String memo) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.allDay = allDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.color = (color != null) ? color : DEFAULT_COLOR;
		this.memo = memo;
	}

}
