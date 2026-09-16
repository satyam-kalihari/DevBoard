CREATE TABLE standup_schedule_days (
                                       standup_id  UUID        NOT NULL,
                                       day_of_week VARCHAR(10) NOT NULL,

                                       CONSTRAINT pk_standup_schedule_days     PRIMARY KEY (standup_id, day_of_week),
                                       CONSTRAINT fk_standup_schedule_standup  FOREIGN KEY (standup_id)
                                           REFERENCES standups (id) ON DELETE CASCADE,
                                       CONSTRAINT chk_standup_schedule_day     CHECK (day_of_week IN (
                                                                                                      'MONDAY','TUESDAY','WEDNESDAY',
                                                                                                      'THURSDAY','FRIDAY','SATURDAY','SUNDAY'
                                           ))
);