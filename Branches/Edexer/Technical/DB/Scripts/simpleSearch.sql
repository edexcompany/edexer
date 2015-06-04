USE `edexer`;
DROP procedure IF EXISTS `simpl_search`;

DELIMITER $$
USE `edexer`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `simpl_search`(IN searchKeyWord varchar(150), IN userId INT)
BEGIN

select 
	bc.bc_first_name 
from 
	edexer.business_card as bc
	left outer join edexer.email as email
	on bc.business_card_id = email.bc_id

	left outer join edexer.fax as fax
	on bc.business_card_id = fax.bc_id

	left outer join edexer.website as website
	on bc.business_card_id = website.bc_id

	left outer join edexer.mobile as mobile
	on bc.business_card_id = mobile.bc_id

	left outer join edexer.social_network as social_network
	on bc.business_card_id = social_network.bc_id

	left outer join edexer.phone as phone
	on bc.business_card_id = phone.bc_id

	left outer join edexer.im as im
	on bc.business_card_id = im.bc_id

	left outer join edexer.bc_tags as bc_tags
	on bc.business_card_id = bc_tags.bc_id

	left outer join edexer.title as title
	on bc.business_card_id = title.bc_id

, tags as tags
where
	tags.tag_id = bc_tags.tags_id
and
	bc.bc_first_name like CONCAT('%' + searchKeyWord +'%')
	or
	bc.middle_name like CONCAT('%' + searchKeyWord +'%')
	or 
	bc.bc_last_name like CONCAT('%' + searchKeyWord +'%')
	or
	email.email_address like CONCAT('%' + searchKeyWord +'%')
	or
	fax.fax_num like CONCAT('%' + searchKeyWord +'%')
	or
	title.company like CONCAT('%' + searchKeyWord +'%')
	or
	title.department like CONCAT('%' + searchKeyWord +'%')
	or
	title.title like CONCAT('%' + searchKeyWord +'%')
	or
	tags.tag_name like CONCAT('%' + searchKeyWord +'%')
	or
	im.identifier like CONCAT('%' + searchKeyWord +'%')
	or
	im.network like CONCAT('%' + searchKeyWord +'%')
	or
	phone.phone_num like CONCAT('%' + searchKeyWord +'%')
	or
	social_network.identifier like CONCAT('%' + searchKeyWord +'%')
	or
	mobile.mobile_num like CONCAT('%' + searchKeyWord +'%')
	or
	website.website like CONCAT('%' + searchKeyWord +'%')
	or
	fax.fax_num like CONCAT('%' + searchKeyWord +'%')
	or
	email.email_address like CONCAT('%' + searchKeyWord +'%')
	and
	bc.subscription_user_id = userId;

END$$

DELIMITER ;

