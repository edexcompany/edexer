use edexer;
drop procedure if exists `advancedFilter`;

delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `advancedFilter`(
		 IN page_size int
		,IN offsetCount int
		,IN user_id_param int
        ,IN sub_type_param int
        ,IN first_name_param varchar(50) 
        ,IN last_name_param varchar(50)
        ,IN title_param varchar(45)
        ,IN company_param varchar(100)
        ,IN mobile_param varchar(20)
        ,IN email_param varchar(150)
        ,IN sector_name_param varchar(50)
        ,IN tags_param varchar(50)
        ,IN order_by_param int
        ,IN owner_id_param int
        ,IN keyword_param varchar(150) 
        ,IN country_param varchar(100) 
        ,IN state_param varchar(100) 
        ,IN city_param varchar(100) 
        )
BEGIN
 
 #sub_type_param
 # -1
DECLARE sub_type_free int(10) DEFAULT 1;
DECLARE sub_type_pro int(10) DEFAULT 2;
DECLARE sub_type_corp int(10) DEFAULT 3;


SELECT DISTINCT
    (bc.business_card_id),
    bc.bc_first_name,
    bc.bc_last_name,
    bc.avatar,
    bc.birth_date,
    bc.business_card_id,
    bc.creator,
    bc.middle_name,
    bc.notes,
    bc.subscription_sub_type,
    bc.subscription_user_id
FROM
    edexer.business_card AS bc
        LEFT JOIN
    edexer.user_subscription AS us ON bc.subscription_sub_type = us.sub_type
        AND bc.subscription_user_id = us.user_id
        LEFT JOIN
    edexer.address AS address ON bc.business_card_id = address.business_card_business_card_id
        LEFT JOIN
    edexer.countries AS countries ON address.country = countries.idCountry
        LEFT JOIN
    edexer.bc_tags AS tags ON bc.business_card_id = tags.bc_id
        LEFT JOIN
    edexer.email AS email ON bc.business_card_id = email.bc_id
        LEFT JOIN
    edexer.mobile mob ON bc.business_card_id = mob.bc_id
        LEFT JOIN
    edexer.title AS title ON bc.business_card_id = title.bc_id
        LEFT JOIN
    edexer.sector AS sector ON title.sector_sector_id = sector.sector_id
WHERE
    (CASE
    #subscriptions part
        WHEN
            sub_type_param = 1
            #corp
        THEN
            (bc.subscription_sub_type = sub_type_corp
                AND bc.subscription_user_id = owner_id_param)
        WHEN
            sub_type_param = 2
            #personal
        THEN
            ((bc.subscription_sub_type = sub_type_free
                OR bc.subscription_sub_type = sub_type_pro)
                AND bc.subscription_user_id = user_id_param)
        WHEN
            sub_type_param = 3
            #personal + corp
        THEN
            (((bc.subscription_sub_type = sub_type_free
                OR bc.subscription_sub_type = sub_type_pro)
                AND bc.subscription_user_id = user_id_param)
                OR (bc.subscription_sub_type = sub_type_corp
                AND bc.subscription_user_id = owner_id_param))
        WHEN
            sub_type_param = 4
            #findOn
        THEN
            (bc.subscription_sub_type IS NULL
                OR bc.subscription_user_id IS NULL)
        WHEN
            sub_type_param = 5
            #findOn + corp
        THEN
            ((bc.subscription_sub_type IS NULL
                OR bc.subscription_user_id IS NULL)
                OR (bc.subscription_sub_type = sub_type_corp
                AND bc.subscription_user_id = owner_id_param))
        WHEN
            sub_type_param = 6
            #findOn + personal
        THEN
            ((bc.subscription_sub_type IS NULL
                OR bc.subscription_user_id IS NULL)
                OR ((bc.subscription_sub_type = sub_type_free
                OR bc.subscription_sub_type = sub_type_pro)
                AND bc.subscription_user_id = user_id_param))
        WHEN
            sub_type_param = 7
            #findOn + personal + corp
        THEN
            ((bc.subscription_sub_type IS NULL
                OR bc.subscription_user_id IS NULL)
                OR ((bc.subscription_sub_type = sub_type_free
                OR bc.subscription_sub_type = sub_type_pro)
                AND bc.subscription_user_id = user_id_param)
                OR (bc.subscription_sub_type = sub_type_corp
                AND bc.subscription_user_id = owner_id_param))
        ELSE 0
    END)
        AND (CASE
        WHEN
            keyword_param != ''
            #keyword / simple search
        THEN
            (bc.bc_first_name LIKE CONCAT('%' , keyword_param , '%')
                OR bc.middle_name LIKE CONCAT('%' , keyword_param , '%')
                OR bc.bc_last_name LIKE CONCAT('%' ,keyword_param , '%')
                OR email.email_address LIKE CONCAT('%' , keyword_param , '%')
                OR title.company LIKE CONCAT('%' , keyword_param , '%')
                OR title.department LIKE CONCAT('%' , keyword_param , '%')
                OR title.title LIKE CONCAT('%' , keyword_param , '%')
                #OR tags.tag_name LIKE CONCAT('%' , keyword_param, '%')
                #OR mobile.mobile_num LIKE CONCAT('%' , keyword_param , '%')
                )
        ELSE 1
    END)    
        AND bc.bc_first_name LIKE CONCAT('%', first_name_param, '%')
        AND bc.bc_last_name LIKE CONCAT('%', last_name_param, '%')
        AND (CASE
        WHEN mobile_param != '' THEN mob.mobile_num LIKE CONCAT('%', mobile_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN email_param != '' THEN email.email_address LIKE CONCAT('%', email_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN title_param != '' THEN title.title LIKE CONCAT('%', title_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN title_param != '' THEN title.title LIKE CONCAT('%', title_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN company_param != '' THEN title.company LIKE CONCAT('%', company_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN sector_name_param != '' THEN sector.sector_name LIKE CONCAT('%', sector_name_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN country_param != '' THEN countries.countryName LIKE CONCAT('%', country_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN state_param != '' THEN address.state LIKE CONCAT('%', state_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN city_param != '' THEN address.city LIKE CONCAT('%', city_param, '%')
        ELSE 1
    END)
        AND (CASE
        WHEN tags_param != '' THEN FIND_IN_SET(tags.tags_id, tags_param)
        ELSE 1
    END)

#Order by part
ORDER BY CASE order_by_param
    WHEN 01 THEN bc.bc_first_name
END ASC , CASE order_by_param
    WHEN 02 THEN bc.bc_first_name
END DESC , CASE order_by_param
    WHEN 11 THEN bc.bc_last_name
END ASC , CASE order_by_param
    WHEN 12 THEN bc.bc_last_name
END DESC , CASE order_by_param
    WHEN 21 THEN title.title
END ASC , CASE order_by_param
    WHEN 22 THEN title.title
END DESC , CASE order_by_param
    WHEN 31 THEN title.company
END ASC , CASE order_by_param
    WHEN 32 THEN title.company
END DESC , CASE order_by_param
    WHEN 41 THEN title.sector_sector_id
END ASC , CASE order_by_param
    WHEN 42 THEN title.sector_sector_id
END DESC , CASE order_by_param
    WHEN 51 THEN email.email_address
END ASC , CASE order_by_param
    WHEN 52 THEN email.email_address
END DESC , CASE order_by_param
    WHEN 61 THEN mob.mobile_num
END ASC , CASE order_by_param
    WHEN 62 THEN mob.mobile_num
END DESC , CASE order_by_param
    WHEN 71 THEN address.country
END ASC , CASE order_by_param
    WHEN 72 THEN address.country
END DESC , CASE order_by_param
    WHEN 81 THEN bc.business_card_id
END ASC , CASE order_by_param
    WHEN 82 THEN bc.business_card_id
END DESC

#paging part
LIMIT PAGE_SIZE OFFSET OFFSETCOUNT

;
 
 END //
 delimiter ;



