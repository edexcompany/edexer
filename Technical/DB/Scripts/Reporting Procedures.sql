use edexer;
drop procedure if exists `getSectorsCountByUserSub`;

delimiter //
CREATE PROCEDURE getSectorsCountByUserSub(
		IN user_id int,
        IN sub_type int,
        IN country_id int
        )
BEGIN
select s.sector_id,s.sector_name,count(*) as Sector_Count
from title t 
inner join sector s on t.sector_sector_id=s.sector_id
inner join business_card b on t.bc_id=b.business_card_id
Left outer join address on b.business_card_id=address.business_card_business_card_id
Left outer join countries on  address.country=countries.idCountry
where b.subscription_user_id=user_id and b.subscription_sub_type=sub_type
AND (CASE WHEN  country_id !=0 THEN countries.idCountry=country_id ELSE 1   END)
group by s.sector_id;
END//
delimiter ;


use edexer;
drop procedure if exists `getSectorsCountByUserSubCountry`;
delimiter //
CREATE PROCEDURE getSectorsCountByUserSubCountry(
		IN user_id int
		,IN sub_type int
        ,In country_id int
        ,IN sector_id int
        )
BEGIN
select title,count(*)
from title t 
inner join sector s on t.sector_sector_id=s.sector_id
inner join business_card b on t.bc_id=b.business_card_id
Left outer join address on b.business_card_id=address.business_card_business_card_id
Left outer join countries on  address.country=countries.idCountry
where
	   ( (CASE WHEN  user_id != 0   THEN b.subscription_user_id=user_id ELSE 0  END) 
	AND (CASE WHEN  sub_type !=0   THEN b.subscription_sub_type=sub_type ELSE 0 END) 
	AND (CASE WHEN  country_id !=0 THEN countries.idCountry=country_id ELSE 1   END)
	AND (case WHEN 	sector_id !=0  THEN s.sector_id=sector_id ELSE 1 END))
group by t.title;
END//
delimiter ;