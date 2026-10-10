
ALTER TABLE notifications DROP CONSTRAINT IF EXISTS chk_notifications_type;
ALTER TABLE notifications ADD CONSTRAINT chk_notifications_type
    CHECK (type IN (
                    'TASK_ASSIGNED', 'TASK_UNASSIGNED', 'TASK_COMMENT_MENTION',
                    'STANDUP_REMINDER', 'SPRINT_STARTED', 'SPRINT_ENDED',
                    'BLOCKER_ADDED', 'MEMBER_INVITED'
        ));