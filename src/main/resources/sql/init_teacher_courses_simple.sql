-- Insert default semester
INSERT INTO edu_semester (semester_name, semester_code, start_date, end_date, is_current, status) 
VALUES ('2024-2025 Semester 1', '2024-2025-1', '2024-09-01', '2025-01-15', '1', '1')
ON DUPLICATE KEY UPDATE semester_name = semester_name;

-- Insert course releases for teacher ID=2
INSERT INTO edu_course_release (course_id, semester_id, teacher_id, capacity, selected_count, release_status, start_week, end_week, class_time, class_location)
SELECT 8, 1, 2, 60, 0, '1', 1, 18, 'Monday 1-2', 'Building A101'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM edu_course_release WHERE course_id = 8 AND teacher_id = 2 AND semester_id = 1
);

INSERT INTO edu_course_release (course_id, semester_id, teacher_id, capacity, selected_count, release_status, start_week, end_week, class_time, class_location)
SELECT 9, 1, 2, 50, 0, '1', 1, 18, 'Wednesday 3-4', 'Building B202'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM edu_course_release WHERE course_id = 9 AND teacher_id = 2 AND semester_id = 1
);

-- Insert course releases for teacher ID=5
INSERT INTO edu_course_release (course_id, semester_id, teacher_id, capacity, selected_count, release_status, start_week, end_week, class_time, class_location)
SELECT 8, 1, 5, 45, 0, '1', 1, 18, 'Tuesday 1-2', 'Building C303'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM edu_course_release WHERE course_id = 8 AND teacher_id = 5 AND semester_id = 1
);
