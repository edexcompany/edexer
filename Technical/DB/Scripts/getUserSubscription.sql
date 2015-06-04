use edexer;
delimiter //
CREATE PROCEDURE getUserSubscription(IN userId int,IN subType int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_type = subType ;
 END //
 delimiter ;



delimiter //
CREATE PROCEDURE getUserSubscriptionsByUserId(IN userId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId;
 END //
 delimiter ;
 
 
 delimiter //
CREATE PROCEDURE getUserSubscriptionsByUserIdAndState(IN userId int, IN statusId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_status = statusId ;
 END //
 delimiter ;
 
 
 
  delimiter //
CREATE PROCEDURE getUserSubscriptionsByStatus(IN statusId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.sub_status = statusId ;
 END //
 delimiter ;
 
 
 
  delimiter //
CREATE PROCEDURE getUserSubscriptionByUserIdAndType(IN userId int, IN subscriptionType int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_type = subscriptionType ;
 END //
 delimiter ;
 
 
 
   delimiter //
CREATE PROCEDURE getTagsByUserSubscription(IN user_id int, IN sub_type int)
 BEGIN
 SELECT * 
 FROM tags
 WHERE tags.user_id = user_id and tags.sub_type = sub_type ;
 END //
 delimiter ;
 
 
   delimiter //
CREATE PROCEDURE getMobileByIdAndSubscription(IN mobileNum varchar(20), IN user_id int, IN sub_type int)
 BEGIN
select * from Mobile as m
inner join business_card as bc on m.bc_id = bc.business_card_id
where m.mobile_num like mobileNum
and bc.subscription_sub_type = sub_type
and bc.subscription_user_id = user_id;
 END //
 delimiter ;

 
   delimiter //
CREATE PROCEDURE getPhoneByIdAndSubscription(IN phoneNum varchar(20), IN user_id int, IN sub_type int)
 BEGIN
select * from Phone as p
inner join business_card as bc on p.bc_id = bc.business_card_id
where p.phone_num like phoneNum
and bc.subscription_sub_type = sub_type
and bc.subscription_user_id = user_id;
 END //
 delimiter ;

 
   delimiter //
CREATE PROCEDURE getFaxByIdAndSubscription(IN faxNum varchar(20), IN user_id int, IN sub_type int)
 BEGIN
select * from Fax as f
inner join business_card as bc on f.bc_id = bc.business_card_id
where f.fax_num like faxNum
and bc.subscription_sub_type = sub_type
and bc.subscription_user_id = user_id;
 END //
 delimiter ;
 
 
 
   delimiter //
CREATE PROCEDURE getEmailByIdAndSubscription(IN emailAddress varchar(150), IN user_id int, IN sub_type int)
 BEGIN
select * from email as e
inner join business_card as bc on e.bc_id = bc.business_card_id
where e.email_address like emailAddress
and bc.subscription_sub_type = sub_type
and bc.subscription_user_id = user_id;
 END //
 delimiter ;

 delimiter //
CREATE PROCEDURE getUsersForCorporateSubscription(IN ownerId int)
 BEGIN
 select * from user as u
inner join user_subscription as us on u.user_id = us.user_id
where us.subscription_owner = ownerId;
 END //
 delimiter ;