create or replace table category
(
    id    int auto_increment
        primary key,
    label varchar(50) null
);

create or replace table member
(
    id        int auto_increment
        primary key,
    pseudo    varchar(20)  not null,
    lastname  varchar(50)  not null,
    firstname varchar(50)  not null,
    email     varchar(255) not null,
    phone     varchar(20)  not null,
    street    varchar(50)  not null,
    zipCode   varchar(20)  not null,
    city      varchar(50)  not null,
    password  varchar(255) not null,
    credits   int          not null,
    isAdmin   tinyint(1)   not null,
    isEnabled tinyint(1)   not null,
    constraint member_unique
        unique (pseudo)
);

create or replace table article
(
    id                 int auto_increment
        primary key,
    name               varchar(50)                                                          not null,
    description        varchar(255)                                                         null,
    auction_start_date datetime                                                             not null,
    auction_end_date   datetime                                                             not null,
    initial_price      int                                                                  not null,
    sell_price         int                                                                  not null,
    seller_id          int                                                                  null,
    state              enum ('UNSTARTED', 'IN_PROGRESS', 'CANCELED', 'EXPIRED', 'FINISHED') not null,
    buyer_id           int                                                                  null,
    category_id        int                                                                  not null,
    constraint article_buyer_id_fk
        foreign key (buyer_id) references member (id),
    constraint article_category_id_fk
        foreign key (category_id) references category (id),
    constraint article_seller_id_fk
        foreign key (seller_id) references member (id)
);

create or replace table auction
(
    id         int auto_increment
        primary key,
    date       timestamp default current_timestamp() not null on update current_timestamp(),
    amount     int                                   not null,
    member_id  int                                   not null,
    article_id int                                   not null,
    constraint auction_article_id_fk
        foreign key (article_id) references article (id),
    constraint auction_member_id_fk
        foreign key (member_id) references member (id)
);

create or replace table reset_token
(
    id        int auto_increment
        primary key,
    token     varchar(255) not null,
    member_id int          not null,
    expiry    datetime     not null,
    constraint reset_token_member_id_fk
        foreign key (member_id) references member (id)
            on delete cascade
);

create or replace table withdrawal
(
    article_id int         not null
        primary key,
    street     varchar(50) not null,
    zipCode    varchar(20) null,
    city       varchar(50) not null,
    constraint withdrawal_article_id_fk
        foreign key (article_id) references article (id)
);

create or replace procedure UpdateAuctionStatus()
BEGIN
    UPDATE article
    SET state='UNSTARTED'
    WHERE auction_start_date > current_date
      AND NOT state IN ('CANCELED', 'UNSTARTED');

    UPDATE article
    SET state='IN_PROGRESS'
    WHERE auction_start_date <= current_date
      AND auction_end_date >= current_date
      AND NOT state IN ('CANCELED', 'IN_PROGRESS');

    CALL UpdateFinishedSales();

    UPDATE article
    SET state='EXPIRED'
    WHERE auction_end_date < current_date
      AND buyer_id = seller_id
      AND NOT state IN ('CANCELED', 'EXPIRED', 'FINISHED');

    UPDATE article
    SET state='FINISHED'
    WHERE auction_end_date < current_date
      AND NOT buyer_id = seller_id
      AND NOT state IN ('CANCELED', 'FINISHED', 'EXPIRED');
END;

create or replace procedure UpdateFinishedSales()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE articleIdVar INT;
    DECLARE memberIdVar INT;
    DECLARE sellPriceVar INT;

    DECLARE cur CURSOR FOR
        SELECT DISTINCT id FROM article WHERE auction_end_date < current_date AND NOT
            (initial_price = sell_price AND buyer_id = seller_id) AND state IN ('IN_PROGRESS');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO articleIdVar;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT sell_price INTO sellPriceVar FROM article WHERE id=articleIdVar;
        SELECT seller_id INTO memberIdVar FROM article WHERE id=articleIdVar;

        UPDATE member SET credits = credits + sellPriceVar WHERE id = memberIdVar;
    END LOOP;

    CLOSE cur;
END;