-- Manual migration: audit_log_entry.message (free text) -> audit_log_entry.action (AuditAction enum).
-- Run this yourself against the target database, e.g.:
--   psql "$DATABASE_URL" -f scripts/audit_log_entry_action_migration.sql
--
-- This is NOT a Flyway migration file (not placed under backend/src/main/resources/db/migration) so it will
-- not be auto-applied when the backend starts. Run it once, manually, before deploying the backend build that
-- expects the `action` column (AuditLogEntryData.action).
--
-- Known limitation: gateway/backoffice/registration/RegistrationGuardService.addParticipant(auth, id, accountId, request)
-- used to log the wrong label ("removeParticipant" instead of "addParticipant") due to a copy-paste bug that is now
-- fixed in code. Existing rows with that exact combination cannot be told apart from genuine removals by their data
-- (identical source, identical empty request payload), so they are left mapped to REGISTRATION_PARTICIPANT_REMOVED.

BEGIN;

ALTER TABLE audit_log_entry
    ADD COLUMN action VARCHAR(255);

-- unambiguous 1:1 renames
UPDATE audit_log_entry SET action = 'CREATE' WHERE message = 'CREATE';
UPDATE audit_log_entry SET action = 'UPDATE' WHERE message = 'UPDATE';
UPDATE audit_log_entry SET action = 'EVENT_CANCELLED' WHERE message = 'CANCELLED';
UPDATE audit_log_entry SET action = 'EVENT_STATUS_CHANGED' WHERE message = 'STATUS';
UPDATE audit_log_entry SET action = 'EVENT_PUBLISH_CHANGED' WHERE message = 'PUBLISH';
UPDATE audit_log_entry SET action = 'EVENT_FEATURED_CHANGED' WHERE message = 'FEATURED';
UPDATE audit_log_entry SET action = 'EVENT_BOOKMARK_ADDED' WHERE message = 'BOOKMARK';
UPDATE audit_log_entry SET action = 'EVENT_BOOKMARK_REMOVED' WHERE message = 'UNBOOKMARK';
UPDATE audit_log_entry SET action = 'EVENT_TITLE_CHANGED' WHERE message = 'TITLE';
UPDATE audit_log_entry SET action = 'EVENT_SHORT_TEXT_CHANGED' WHERE message = 'SHORT_TEXT';
UPDATE audit_log_entry SET action = 'EVENT_LONG_TEXT_CHANGED' WHERE message = 'LONG_TEXT';
UPDATE audit_log_entry SET action = 'EVENT_TAGS_CHANGED' WHERE message = 'TAGS';
UPDATE audit_log_entry SET action = 'EVENT_TEXT_CHANGED' WHERE message = 'TEXT';
UPDATE audit_log_entry SET action = 'EVENT_CATEGORIES_CHANGED' WHERE message = 'CATEGORIES';
UPDATE audit_log_entry SET action = 'EVENT_AUDIENCES_CHANGED' WHERE message = 'AUDIENCES';
UPDATE audit_log_entry SET action = 'EVENT_ANNOUNCEMENT_ADDED' WHERE message = 'announcement';
UPDATE audit_log_entry SET action = 'REGISTRATION_PARTICIPANT_ADDED' WHERE message = 'addParticipant';
UPDATE audit_log_entry SET action = 'REGISTRATION_PARTICIPANT_CHANGED' WHERE message = 'changeParticipant';
UPDATE audit_log_entry SET action = 'REGISTRATION_PARTICIPANT_REMOVED' WHERE message = 'removeParticipant';
UPDATE audit_log_entry SET action = 'ADDRESS_SET_DEFAULT' WHERE message = 'Set default address';

-- 'PUBLISHED' was reused for two different actions depending on which gateway logged it:
--   portal EventGuardService.setShared        (source 'APP Event API')        -> EVENT_SHARED_CHANGED
--   backoffice EventGuardService.setPublished (source 'BACKOFFICE Event API') -> EVENT_PUBLISHED_CHANGED
UPDATE audit_log_entry SET action = 'EVENT_SHARED_CHANGED' WHERE message = 'PUBLISHED' AND source = 'APP Event API';
UPDATE audit_log_entry SET action = 'EVENT_PUBLISHED_CHANGED' WHERE message = 'PUBLISHED' AND source = 'BACKOFFICE Event API';

-- 'DELETE' was used both for generic entity deletion (traceDelete, every guard service) and for removing an
-- announcement from an event (portal EventGuardService.deleteAnnouncement). The latter is the only DELETE row
-- logged with an announcement id as its request payload (a bare JSON number), so that shape tells them apart.
UPDATE audit_log_entry SET action = 'EVENT_ANNOUNCEMENT_REMOVED' WHERE message = 'DELETE' AND source = 'APP Event API' AND request ~ '^[0-9]+$';
UPDATE audit_log_entry SET action = 'DELETE' WHERE message = 'DELETE' AND action IS NULL;

-- safety net: fail loudly instead of silently dropping rows with an unrecognized legacy message
DO $$
    DECLARE unmapped BIGINT;
    BEGIN
        SELECT count(*) INTO unmapped FROM audit_log_entry WHERE action IS NULL;
        IF unmapped > 0 THEN
            RAISE EXCEPTION 'audit_log_entry migration: % row(s) have no mapped action, inspect message/source before proceeding', unmapped;
        END IF;
    END
$$;

ALTER TABLE audit_log_entry ALTER COLUMN action SET NOT NULL;

ALTER TABLE audit_log_entry DROP COLUMN message;

COMMIT;
