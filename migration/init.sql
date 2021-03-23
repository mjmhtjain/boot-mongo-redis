DROP TABLE IF EXISTS CARTITEM;
CREATE TABLE CARTITEM
  (
     id         BIGINT NOT NULL,
     userid     BIGINT NOT NULL,
     itemid     BIGINT NOT NULL,
     itemname   VARCHAR(255),
     quantity   BIGINT NOT NULL,
     PRIMARY KEY (id)
  );

DROP SEQUENCE IF EXISTS CartItemSeq;
CREATE SEQUENCE CartItemSeq INCREMENT 1 MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;



DO $FN$
DECLARE
  user_id integer;
  item_id integer;
BEGIN
  FOR counter IN 1..10000 LOOP
    RAISE NOTICE 'Counter: %', counter;

    EXECUTE $$  SELECT floor(random() * 5000)::int $$
      INTO user_id;

    EXECUTE $$  SELECT floor(random() * 5000)::int $$
      INTO item_id;

    EXECUTE $$  Insert Into CartItem (id,userid,itemid,itemname,quantity) VALUES (nextval('CartItemSeq'),$1,$2,'item',2) $$
      USING user_id, item_id;
  END LOOP;
END;
$FN$