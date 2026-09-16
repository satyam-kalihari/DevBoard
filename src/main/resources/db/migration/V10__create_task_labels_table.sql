CREATE TABLE task_labels (
                             task_id     UUID    NOT NULL,
                             label_id    UUID    NOT NULL,

                             CONSTRAINT pk_task_labels           PRIMARY KEY (task_id, label_id),
                             CONSTRAINT fk_task_labels_task      FOREIGN KEY (task_id)
                                 REFERENCES tasks (id) ON DELETE CASCADE,
                             CONSTRAINT fk_task_labels_label     FOREIGN KEY (label_id)
                                 REFERENCES labels (id) ON DELETE CASCADE
);