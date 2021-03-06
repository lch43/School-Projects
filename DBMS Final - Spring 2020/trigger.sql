--Landon Higinbotham
--LCH43
--CS1555
CREATE OR REPLACE TRIGGER ASSIGN_MEDAL
    BEFORE UPDATE OR INSERT
    ON SCOREBOARD
    FOR EACH ROW
BEGIN
    IF :new.POSITION <= 3 AND :new.POSITION>=1
    THEN
        :new.MEDAL_ID := :new.POSITION;
    ELSE :new.MEDAL_ID := NULL;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER ATHELETE_DISMISSAL
    BEFORE DELETE
    ON PARTICIPANT
    FOR EACH ROW
BEGIN
    UPDATE EVENT_PARTICIPATION
    SET STATUS = 'n'
    WHERE TEAM_ID = (SELECT TEAM_ID
    FROM TEAM_MEMBER
    WHERE TEAM_MEMBER.PARTICIPANT_ID = :OLD.PARTICIPANT_ID);
    
    DELETE FROM TEAM WHERE TEAM_ID =
    (SELECT TEAM_ID FROM (SELECT TEAM_ID, COUNT(PARTICIPANT_ID) AS PART_COUNT
    FROM TEAM_MEMBER
    WHERE TEAM_ID = (SELECT TEAM_ID
    FROM TEAM_MEMBER
    WHERE TEAM_MEMBER.PARTICIPANT_ID = :OLD.PARTICIPANT_ID)
    GROUP BY TEAM_ID)
    WHERE PART_COUNT = 1);
    
    DELETE FROM SCOREBOARD WHERE PARTICIPANT_ID = :OLD.PARTICIPANT_ID;
    DELETE FROM TEAM_MEMBER WHERE PARTICIPANT_ID = :OLD.PARTICIPANT_ID;
END;
/

CREATE OR REPLACE TRIGGER ENFORCE_CAPACITY
    BEFORE INSERT
    ON EVENT
    FOR EACH ROW
DECLARE
    VEN_COUNT INTEGER;
    VEN_MAX INTEGER;
BEGIN
    SELECT COUNT(*) INTO VEN_COUNT
    FROM EVENT
    WHERE VENUE_ID = :NEW.VENUE_ID AND EVENT_TIME = :NEW.EVENT_TIME;
    
    SELECT CAPACITY INTO VEN_MAX
    FROM VENUE
    WHERE VENUE_ID = :NEW.VENUE_ID;
    
    IF VEN_COUNT+1 > VEN_MAX
    THEN
        RAISE_APPLICATION_ERROR( -20001, 
                             'The venue capacity has been reached' );
    END IF;
END;
/

DROP SEQUENCE USER_ID_SEQ;
CREATE SEQUENCE USER_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE OLYMPIC_ID_SEQ;
CREATE SEQUENCE OLYMPIC_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE SPORT_ID_SEQ;
CREATE SEQUENCE SPORT_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE TEAM_ID_SEQ;
CREATE SEQUENCE TEAM_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE VENUE_ID_SEQ;
CREATE SEQUENCE VENUE_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE EVENT_ID_SEQ;
CREATE SEQUENCE EVENT_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/

DROP SEQUENCE PARTICIPANT_ID_SEQ;
CREATE SEQUENCE PARTICIPANT_ID_SEQ
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  NOCACHE;
/