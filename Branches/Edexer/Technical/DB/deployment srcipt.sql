-- Initial FindOn Database script
-- 21/2/15 
-- updates on schema to be added in incremental sql scripts


SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema edexer_edexer
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema edexer_edexer
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `edexer_edexer`;
CREATE SCHEMA IF NOT EXISTS `edexer_edexer` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `edexer_edexer` ;

-- -----------------------------------------------------
-- Table `edexer_edexer`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`user` (
  `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_email` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL,
  `first_name` VARCHAR(50) NULL,
  `last_name` VARCHAR(50) NULL,
  `password` VARCHAR(50) NOT NULL,
  `mobile` VARCHAR(20) NULL,
  `phone` VARCHAR(20) NULL,
  `title` VARCHAR(75) NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `edexer_edexer`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`role` (
  `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(50) NOT NULL,
  `role_desc` VARCHAR(50) NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`subscription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`subscription` (
  `subscription_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `subscription_type_name` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(100) NULL,
  PRIMARY KEY (`subscription_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`subscription_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`subscription_status` (
  `sub_status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `sub_status_name` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`sub_status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`act_as`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`act_as` (
  `act_as_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `act_as_name` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(100) NULL,
  PRIMARY KEY (`act_as_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`last_edit_reason`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`last_edit_reason` (
  `reason_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `reason_name` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`reason_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`user_subscription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`user_subscription` (
  `user_id` INT UNSIGNED NOT NULL,
  `sub_type` INT UNSIGNED NOT NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `subscription_date` DATE NULL,
  `act_as` INT UNSIGNED NOT NULL,
  `last_edit_date` DATE NULL,
  `last_edit_by` INT UNSIGNED NOT NULL,
  `last_edit_reason` INT UNSIGNED NULL,
  `sub_status` INT UNSIGNED NOT NULL,
  `note` VARCHAR(100) NULL,
  PRIMARY KEY (`user_id`, `sub_type`),
  INDEX `fk_user_subscription_User1_idx` (`user_id` ASC),
  INDEX `fk_user_subscription_subscription1_idx` (`sub_type` ASC),
  INDEX `fk_user_subscription_User2_idx` (`last_edit_by` ASC),
  INDEX `fk_user_subscription_subscription_status1_idx` (`sub_status` ASC),
  INDEX `fk_user_subscription_act_as1_idx` (`act_as` ASC),
  INDEX `fk_user_subscription_last_edit_reason1_idx` (`last_edit_reason` ASC),
  CONSTRAINT `fk_user_subscription_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `edexer_edexer`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_subscription_subscription1`
    FOREIGN KEY (`sub_type`)
    REFERENCES `edexer_edexer`.`subscription` (`subscription_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_subscription_User2`
    FOREIGN KEY (`last_edit_by`)
    REFERENCES `edexer_edexer`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_subscription_subscription_status1`
    FOREIGN KEY (`sub_status`)
    REFERENCES `edexer_edexer`.`subscription_status` (`sub_status_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_subscription_act_as1`
    FOREIGN KEY (`act_as`)
    REFERENCES `edexer_edexer`.`act_as` (`act_as_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_subscription_last_edit_reason1`
    FOREIGN KEY (`last_edit_reason`)
    REFERENCES `edexer_edexer`.`last_edit_reason` (`reason_id`)
    ON DELETE SET NULL
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`business_card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`business_card` (
  `business_card_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `bc_first_name` VARCHAR(50) NULL,
  `middle_name` VARCHAR(50) NULL,
  `bc_last_name` VARCHAR(50) NULL,
  `birth_date` VARCHAR(50) NULL,
  `notes` VARCHAR(250) NULL,
  `creator` INT NOT NULL,
  PRIMARY KEY (`business_card_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`user_role` (
  `user_user_id` INT UNSIGNED NOT NULL,
  `role_role_id` INT UNSIGNED NOT NULL,
  `notes` VARCHAR(45) NULL,
  PRIMARY KEY (`user_user_id`, `role_role_id`),
  INDEX `fk_User_has_role_role1_idx` (`role_role_id` ASC),
  INDEX `fk_User_has_role_User_idx` (`user_user_id` ASC),
  CONSTRAINT `fk_User_has_role_User`
    FOREIGN KEY (`user_user_id`)
    REFERENCES `edexer_edexer`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_role_role1`
    FOREIGN KEY (`role_role_id`)
    REFERENCES `edexer_edexer`.`role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`tags` (
  `tag_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `sub_type` INT UNSIGNED NOT NULL,
  `tag_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`tag_id`),
  INDEX `fk_tags_user_subscription1_idx` (`user_id` ASC, `sub_type` ASC),
  CONSTRAINT `fk_tags_user_subscription1`
    FOREIGN KEY (`user_id` , `sub_type`)
    REFERENCES `edexer_edexer`.`user_subscription` (`user_id` , `sub_type`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`attachment` (
  `attachment_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(150) NOT NULL,
  `desc` VARCHAR(50) NULL,
  `business_card_business_card_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`attachment_id`, `business_card_business_card_id`),
  INDEX `fk_attachment_business_card1_idx` (`business_card_business_card_id` ASC),
  CONSTRAINT `fk_attachment_business_card1`
    FOREIGN KEY (`business_card_business_card_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`mobile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`mobile` (
  `bc_id` INT UNSIGNED NOT NULL,
  `mobile_num` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`bc_id`, `mobile_num`),
  CONSTRAINT `fk_mobile_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`website`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`website` (
  `bc_id` INT UNSIGNED NOT NULL,
  `website` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`website`, `bc_id`),
  CONSTRAINT `fk_website_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`phone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`phone` (
  `bc_id` INT UNSIGNED NOT NULL,
  `phone_num` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`bc_id`, `phone_num`),
  CONSTRAINT `fk_phone_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`fax`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`fax` (
  `bc_id` INT UNSIGNED NOT NULL,
  `fax_num` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`bc_id`, `fax_num`),
  CONSTRAINT `fk_fax_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`email`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`email` (
  `bc_id` INT UNSIGNED NOT NULL,
  `email_address` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`bc_id`, `email_address`),
  CONSTRAINT `fk_email_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`im`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`im` (
  `bc_id` INT UNSIGNED NOT NULL,
  `identifier` VARCHAR(50) NOT NULL,
  `network` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`identifier`, `bc_id`),
  INDEX `fk_im_business_card1_idx` (`bc_id` ASC),
  CONSTRAINT `fk_im_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`address` (
  `business_card_business_card_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`business_card_business_card_id`),
  CONSTRAINT `fk_address_business_card1`
    FOREIGN KEY (`business_card_business_card_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`social_networks_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`social_networks_types` (
  `sn_type_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `desc` VARCHAR(45) NULL,
  PRIMARY KEY (`sn_type_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`social_network`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`social_network` (
  `bc_id` INT UNSIGNED NOT NULL,
  `identifier` VARCHAR(50) NOT NULL,
  `type` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`bc_id`, `identifier`),
  INDEX `fk_social_network_business_card1_idx` (`bc_id` ASC),
  INDEX `fk_social_network_social_networks_types1_idx` (`type` ASC),
  CONSTRAINT `fk_social_network_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_social_network_social_networks_types1`
    FOREIGN KEY (`type`)
    REFERENCES `edexer_edexer`.`social_networks_types` (`sn_type_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`sector`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`sector` (
  `sector_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `sector_name` VARCHAR(50) NULL,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`sector_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`title`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`title` (
  `title_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `bc_id` INT UNSIGNED NOT NULL,
  `company` VARCHAR(100) NULL,
  `department` VARCHAR(45) NULL,
  `title` VARCHAR(100) NULL,
  `sector_sector_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`title_id`),
  INDEX `fk_title_sector1_idx` (`sector_sector_id` ASC),
  INDEX `fk_title_business_card1_idx` (`bc_id` ASC),
  CONSTRAINT `fk_title_sector1`
    FOREIGN KEY (`sector_sector_id`)
    REFERENCES `edexer_edexer`.`sector` (`sector_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_title_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`bc_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`bc_tags` (
  `bc_id` INT UNSIGNED NOT NULL,
  `tags_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`bc_id`, `tags_id`),
  INDEX `fk_tags_has_business_card_business_card1_idx` (`bc_id` ASC),
  INDEX `fk_bc_tags_tags1_idx` (`tags_id` ASC),
  CONSTRAINT `fk_tags_has_business_card_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_bc_tags_tags1`
    FOREIGN KEY (`tags_id`)
    REFERENCES `edexer_edexer`.`tags` (`tag_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `edexer_edexer`.`bc_permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`bc_permissions` (
  `bc_permissions_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `bc_id` INT UNSIGNED NOT NULL,
  `user_subscription_user_id` INT UNSIGNED NOT NULL,
  `user_subscription_sub_type` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`bc_permissions_id`),
  INDEX `fk_bc_permissions_business_card1_idx` (`bc_id` ASC),
  INDEX `fk_bc_permissions_user_subscription1_idx` (`user_subscription_user_id` ASC, `user_subscription_sub_type` ASC),
  CONSTRAINT `fk_bc_permissions_business_card1`
    FOREIGN KEY (`bc_id`)
    REFERENCES `edexer_edexer`.`business_card` (`business_card_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bc_permissions_user_subscription1`
    FOREIGN KEY (`user_subscription_user_id` , `user_subscription_sub_type`)
    REFERENCES `edexer_edexer`.`user_subscription` (`user_id` , `sub_type`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

#--------------------------------------------------------
#-- Table `edexer_edexer`.`admin_config`
#--------------------------------------------------------
CREATE  TABLE `edexer_edexer`.`admin_config` (
  `config_key_name` VARCHAR(100) NULL ,
  `config_value` VARCHAR(150) NULL );

#--------------------------------------------------------
ALTER TABLE `edexer_edexer`.`user` ADD COLUMN `user_role` INT UNSIGNED NULL  AFTER `title` ;
ALTER TABLE `edexer_edexer`.`user` ADD COLUMN `activation_str` VARCHAR(200) NULL  AFTER `user_role` ;
ALTER TABLE `edexer_edexer`.`user` ADD COLUMN `user_avatar` VARCHAR(200) NULL  ;
#-------------------------------------------------------
ALTER TABLE `edexer_edexer`.`user` 
  ADD CONSTRAINT `FK_user_role_id`
  FOREIGN KEY (`user_role` )
  REFERENCES `edexer_edexer`.`role` (`role_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_user_role_id_idx` (`user_role` ASC) ;




CREATE TABLE IF NOT EXISTS `edexer_edexer`.`countries` (
	`idCountry` int(5) NOT NULL AUTO_INCREMENT,
	`countryCode` char(2) NOT NULL DEFAULT '',
	`countryName` varchar(45) NOT NULL DEFAULT '',
	`currencyCode` char(3) DEFAULT NULL,
	`languages` varchar(30) DEFAULT NULL,
	PRIMARY KEY (`idCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `countries` (`countryCode`, `countryName`, `currencyCode`, `languages`) VALUES
('AD', 'Andorra', 'EUR', 'ca'),
('AE', 'United Arab Emirates', 'AED', 'ar-AE,fa,en,hi,ur'),
('AF', 'Afghanistan', 'AFN', 'fa-AF,ps,uz-AF,tk'),
('AG', 'Antigua and Barbuda', 'XCD', 'en-AG'),
('AI', 'Anguilla', 'XCD', 'en-AI'),
('AL', 'Albania', 'ALL', 'sq,el'),
('AM', 'Armenia', 'AMD', 'hy'),
('AO', 'Angola', 'AOA', 'pt-AO'),
('AQ', 'Antarctica', '', ''),
('AR', 'Argentina', 'ARS', 'es-AR,en,it,de,fr,gn'),
('AS', 'American Samoa', 'USD', 'en-AS,sm,to'),
('AT', 'Austria', 'EUR', 'de-AT,hr,hu,sl'),
('AU', 'Australia', 'AUD', 'en-AU'),
('AW', 'Aruba', 'AWG', 'nl-AW,es,en'),
('AX', 'Åland', 'EUR', 'sv-AX'),
('AZ', 'Azerbaijan', 'AZN', 'az,ru,hy'),
('BA', 'Bosnia and Herzegovina', 'BAM', 'bs,hr-BA,sr-BA'),
('BB', 'Barbados', 'BBD', 'en-BB'),
('BD', 'Bangladesh', 'BDT', 'bn-BD,en'),
('BE', 'Belgium', 'EUR', 'nl-BE,fr-BE,de-BE'),
('BF', 'Burkina Faso', 'XOF', 'fr-BF'),
('BG', 'Bulgaria', 'BGN', 'bg,tr-BG'),
('BH', 'Bahrain', 'BHD', 'ar-BH,en,fa,ur'),
('BI', 'Burundi', 'BIF', 'fr-BI,rn'),
('BJ', 'Benin', 'XOF', 'fr-BJ'),
('BL', 'Saint Barthélemy', 'EUR', 'fr'),
('BM', 'Bermuda', 'BMD', 'en-BM,pt'),
('BN', 'Brunei', 'BND', 'ms-BN,en-BN'),
('BO', 'Bolivia', 'BOB', 'es-BO,qu,ay'),
('BQ', 'Bonaire', 'USD', 'nl,pap,en'),
('BR', 'Brazil', 'BRL', 'pt-BR,es,en,fr'),
('BS', 'Bahamas', 'BSD', 'en-BS'),
('BT', 'Bhutan', 'BTN', 'dz'),
('BV', 'Bouvet Island', 'NOK', ''),
('BW', 'Botswana', 'BWP', 'en-BW,tn-BW'),
('BY', 'Belarus', 'BYR', 'be,ru'),
('BZ', 'Belize', 'BZD', 'en-BZ,es'),
('CA', 'Canada', 'CAD', 'en-CA,fr-CA,iu'),
('CC', 'Cocos [Keeling] Islands', 'AUD', 'ms-CC,en'),
('CD', 'Democratic Republic of the Congo', 'CDF', 'fr-CD,ln,kg'),
('CF', 'Central African Republic', 'XAF', 'fr-CF,sg,ln,kg'),
('CG', 'Republic of the Congo', 'XAF', 'fr-CG,kg,ln-CG'),
('CH', 'Switzerland', 'CHF', 'de-CH,fr-CH,it-CH,rm'),
('CI', 'Ivory Coast', 'XOF', 'fr-CI'),
('CK', 'Cook Islands', 'NZD', 'en-CK,mi'),
('CL', 'Chile', 'CLP', 'es-CL'),
('CM', 'Cameroon', 'XAF', 'en-CM,fr-CM'),
('CN', 'China', 'CNY', 'zh-CN,yue,wuu,dta,ug,za'),
('CO', 'Colombia', 'COP', 'es-CO'),
('CR', 'Costa Rica', 'CRC', 'es-CR,en'),
('CU', 'Cuba', 'CUP', 'es-CU'),
('CV', 'Cape Verde', 'CVE', 'pt-CV'),
('CW', 'Curacao', 'ANG', 'nl,pap'),
('CX', 'Christmas Island', 'AUD', 'en,zh,ms-CC'),
('CY', 'Cyprus', 'EUR', 'el-CY,tr-CY,en'),
('CZ', 'Czech Republic', 'CZK', 'cs,sk'),
('DE', 'Germany', 'EUR', 'de'),
('DJ', 'Djibouti', 'DJF', 'fr-DJ,ar,so-DJ,aa'),
('DK', 'Denmark', 'DKK', 'da-DK,en,fo,de-DK'),
('DM', 'Dominica', 'XCD', 'en-DM'),
('DO', 'Dominican Republic', 'DOP', 'es-DO'),
('DZ', 'Algeria', 'DZD', 'ar-DZ'),
('EC', 'Ecuador', 'USD', 'es-EC'),
('EE', 'Estonia', 'EUR', 'et,ru'),
('EG', 'Egypt', 'EGP', 'ar-EG,en,fr'),
('EH', 'Western Sahara', 'MAD', 'ar,mey'),
('ER', 'Eritrea', 'ERN', 'aa-ER,ar,tig,kun,ti-ER'),
('ES', 'Spain', 'EUR', 'es-ES,ca,gl,eu,oc'),
('ET', 'Ethiopia', 'ETB', 'am,en-ET,om-ET,ti-ET,so-ET,sid'),
('FI', 'Finland', 'EUR', 'fi-FI,sv-FI,smn'),
('FJ', 'Fiji', 'FJD', 'en-FJ,fj'),
('FK', 'Falkland Islands', 'FKP', 'en-FK'),
('FM', 'Micronesia', 'USD', 'en-FM,chk,pon,yap,kos,uli,woe'),
('FO', 'Faroe Islands', 'DKK', 'fo,da-FO'),
('FR', 'France', 'EUR', 'fr-FR,frp,br,co,ca,eu,oc'),
('GA', 'Gabon', 'XAF', 'fr-GA'),
('GB', 'United Kingdom', 'GBP', 'en-GB,cy-GB,gd'),
('GD', 'Grenada', 'XCD', 'en-GD'),
('GE', 'Georgia', 'GEL', 'ka,ru,hy,az'),
('GF', 'French Guiana', 'EUR', 'fr-GF'),
('GG', 'Guernsey', 'GBP', 'en,fr'),
('GH', 'Ghana', 'GHS', 'en-GH,ak,ee,tw'),
('GI', 'Gibraltar', 'GIP', 'en-GI,es,it,pt'),
('GL', 'Greenland', 'DKK', 'kl,da-GL,en'),
('GM', 'Gambia', 'GMD', 'en-GM,mnk,wof,wo,ff'),
('GN', 'Guinea', 'GNF', 'fr-GN'),
('GP', 'Guadeloupe', 'EUR', 'fr-GP'),
('GQ', 'Equatorial Guinea', 'XAF', 'es-GQ,fr'),
('GR', 'Greece', 'EUR', 'el-GR,en,fr'),
('GS', 'South Georgia and the South Sandwich Islands', 'GBP', 'en'),
('GT', 'Guatemala', 'GTQ', 'es-GT'),
('GU', 'Guam', 'USD', 'en-GU,ch-GU'),
('GW', 'Guinea-Bissau', 'XOF', 'pt-GW,pov'),
('GY', 'Guyana', 'GYD', 'en-GY'),
('HK', 'Hong Kong', 'HKD', 'zh-HK,yue,zh,en'),
('HM', 'Heard Island and McDonald Islands', 'AUD', ''),
('HN', 'Honduras', 'HNL', 'es-HN'),
('HR', 'Croatia', 'HRK', 'hr-HR,sr'),
('HT', 'Haiti', 'HTG', 'ht,fr-HT'),
('HU', 'Hungary', 'HUF', 'hu-HU'),
('ID', 'Indonesia', 'IDR', 'id,en,nl,jv'),
('IE', 'Ireland', 'EUR', 'en-IE,ga-IE'),
('IL', 'Israel', 'ILS', 'he,ar-IL,en-IL,'),
('IM', 'Isle of Man', 'GBP', 'en,gv'),
('IN', 'India', 'INR', 'en-IN,hi,bn,te,mr,ta,ur'),
('IO', 'British Indian Ocean Territory', 'USD', 'en-IO'),
('IQ', 'Iraq', 'IQD', 'ar-IQ,ku,hy'),
('IR', 'Iran', 'IRR', 'fa-IR,ku'),
('IS', 'Iceland', 'ISK', 'is,en,de,da,sv,no'),
('IT', 'Italy', 'EUR', 'it-IT,de-IT,fr-IT,sc,ca,co,sl'),
('JE', 'Jersey', 'GBP', 'en,pt'),
('JM', 'Jamaica', 'JMD', 'en-JM'),
('JO', 'Jordan', 'JOD', 'ar-JO,en'),
('JP', 'Japan', 'JPY', 'ja'),
('KE', 'Kenya', 'KES', 'en-KE,sw-KE'),
('KG', 'Kyrgyzstan', 'KGS', 'ky,uz,ru'),
('KH', 'Cambodia', 'KHR', 'km,fr,en'),
('KI', 'Kiribati', 'AUD', 'en-KI,gil'),
('KM', 'Comoros', 'KMF', 'ar,fr-KM'),
('KN', 'Saint Kitts and Nevis', 'XCD', 'en-KN'),
('KP', 'North Korea', 'KPW', 'ko-KP'),
('KR', 'South Korea', 'KRW', 'ko-KR,en'),
('KW', 'Kuwait', 'KWD', 'ar-KW,en'),
('KY', 'Cayman Islands', 'KYD', 'en-KY'),
('KZ', 'Kazakhstan', 'KZT', 'kk,ru'),
('LA', 'Laos', 'LAK', 'lo,fr,en'),
('LB', 'Lebanon', 'LBP', 'ar-LB,fr-LB,en,hy'),
('LC', 'Saint Lucia', 'XCD', 'en-LC'),
('LI', 'Liechtenstein', 'CHF', 'de-LI'),
('LK', 'Sri Lanka', 'LKR', 'si,ta,en'),
('LR', 'Liberia', 'LRD', 'en-LR'),
('LS', 'Lesotho', 'LSL', 'en-LS,st,zu,xh'),
('LT', 'Lithuania', 'LTL', 'lt,ru,pl'),
('LU', 'Luxembourg', 'EUR', 'lb,de-LU,fr-LU'),
('LV', 'Latvia', 'EUR', 'lv,ru,lt'),
('LY', 'Libya', 'LYD', 'ar-LY,it,en'),
('MA', 'Morocco', 'MAD', 'ar-MA,fr'),
('MC', 'Monaco', 'EUR', 'fr-MC,en,it'),
('MD', 'Moldova', 'MDL', 'ro,ru,gag,tr'),
('ME', 'Montenegro', 'EUR', 'sr,hu,bs,sq,hr,rom'),
('MF', 'Saint Martin', 'EUR', 'fr'),
('MG', 'Madagascar', 'MGA', 'fr-MG,mg'),
('MH', 'Marshall Islands', 'USD', 'mh,en-MH'),
('MK', 'Macedonia', 'MKD', 'mk,sq,tr,rmm,sr'),
('ML', 'Mali', 'XOF', 'fr-ML,bm'),
('MM', 'Myanmar [Burma]', 'MMK', 'my'),
('MN', 'Mongolia', 'MNT', 'mn,ru'),
('MO', 'Macao', 'MOP', 'zh,zh-MO,pt'),
('MP', 'Northern Mariana Islands', 'USD', 'fil,tl,zh'),
('MQ', 'Martinique', 'EUR', 'fr-MQ'),
('MR', 'Mauritania', 'MRO', 'ar-MR,fuc,snk,fr,mey,wo'),
('MS', 'Montserrat', 'XCD', 'en-MS'),
('MT', 'Malta', 'EUR', 'mt,en-MT'),
('MU', 'Mauritius', 'MUR', 'en-MU,bho,fr'),
('MV', 'Maldives', 'MVR', 'dv,en'),
('MW', 'Malawi', 'MWK', 'ny,yao,tum,swk'),
('MX', 'Mexico', 'MXN', 'es-MX'),
('MY', 'Malaysia', 'MYR', 'ms-MY,en,zh,ta,te,ml,pa,th'),
('MZ', 'Mozambique', 'MZN', 'pt-MZ,vmw'),
('NA', 'Namibia', 'NAD', 'en-NA,af,de,hz,naq'),
('NC', 'New Caledonia', 'XPF', 'fr-NC'),
('NE', 'Niger', 'XOF', 'fr-NE,ha,kr,dje'),
('NF', 'Norfolk Island', 'AUD', 'en-NF'),
('NG', 'Nigeria', 'NGN', 'en-NG,ha,yo,ig,ff'),
('NI', 'Nicaragua', 'NIO', 'es-NI,en'),
('NL', 'Netherlands', 'EUR', 'nl-NL,fy-NL'),
('NO', 'Norway', 'NOK', 'no,nb,nn,se,fi'),
('NP', 'Nepal', 'NPR', 'ne,en'),
('NR', 'Nauru', 'AUD', 'na,en-NR'),
('NU', 'Niue', 'NZD', 'niu,en-NU'),
('NZ', 'New Zealand', 'NZD', 'en-NZ,mi'),
('OM', 'Oman', 'OMR', 'ar-OM,en,bal,ur'),
('PA', 'Panama', 'PAB', 'es-PA,en'),
('PE', 'Peru', 'PEN', 'es-PE,qu,ay'),
('PF', 'French Polynesia', 'XPF', 'fr-PF,ty'),
('PG', 'Papua New Guinea', 'PGK', 'en-PG,ho,meu,tpi'),
('PH', 'Philippines', 'PHP', 'tl,en-PH,fil'),
('PK', 'Pakistan', 'PKR', 'ur-PK,en-PK,pa,sd,ps,brh'),
('PL', 'Poland', 'PLN', 'pl'),
('PM', 'Saint Pierre and Miquelon', 'EUR', 'fr-PM'),
('PN', 'Pitcairn Islands', 'NZD', 'en-PN'),
('PR', 'Puerto Rico', 'USD', 'en-PR,es-PR'),
('PS', 'Palestine', 'ILS', 'ar-PS'),
('PT', 'Portugal', 'EUR', 'pt-PT,mwl'),
('PW', 'Palau', 'USD', 'pau,sov,en-PW,tox,ja,fil,zh'),
('PY', 'Paraguay', 'PYG', 'es-PY,gn'),
('QA', 'Qatar', 'QAR', 'ar-QA,es'),
('RE', 'Réunion', 'EUR', 'fr-RE'),
('RO', 'Romania', 'RON', 'ro,hu,rom'),
('RS', 'Serbia', 'RSD', 'sr,hu,bs,rom'),
('RU', 'Russia', 'RUB', 'ru,tt,xal,cau,ady,kv,ce'),
('RW', 'Rwanda', 'RWF', 'rw,en-RW,fr-RW,sw'),
('SA', 'Saudi Arabia', 'SAR', 'ar-SA'),
('SB', 'Solomon Islands', 'SBD', 'en-SB,tpi'),
('SC', 'Seychelles', 'SCR', 'en-SC,fr-SC'),
('SD', 'Sudan', 'SDG', 'ar-SD,en,fia'),
('SE', 'Sweden', 'SEK', 'sv-SE,se,sma,fi-SE'),
('SG', 'Singapore', 'SGD', 'cmn,en-SG,ms-SG,ta-SG'),
('SH', 'Saint Helena', 'SHP', 'en-SH'),
('SI', 'Slovenia', 'EUR', 'sl,sh'),
('SJ', 'Svalbard and Jan Mayen', 'NOK', 'no,ru'),
('SK', 'Slovakia', 'EUR', 'sk,hu'),
('SL', 'Sierra Leone', 'SLL', 'en-SL,men,tem'),
('SM', 'San Marino', 'EUR', 'it-SM'),
('SN', 'Senegal', 'XOF', 'fr-SN,wo,fuc,mnk'),
('SO', 'Somalia', 'SOS', 'so-SO,ar-SO,it,en-SO'),
('SR', 'Suriname', 'SRD', 'nl-SR,en,srn,hns,jv'),
('SS', 'South Sudan', 'SSP', 'en'),
('ST', 'São Tomé and Príncipe', 'STD', 'pt-ST'),
('SV', 'El Salvador', 'USD', 'es-SV'),
('SX', 'Sint Maarten', 'ANG', 'nl,en'),
('SY', 'Syria', 'SYP', 'ar-SY,ku,hy,arc,fr,en'),
('SZ', 'Swaziland', 'SZL', 'en-SZ,ss-SZ'),
('TC', 'Turks and Caicos Islands', 'USD', 'en-TC'),
('TD', 'Chad', 'XAF', 'fr-TD,ar-TD,sre'),
('TF', 'French Southern Territories', 'EUR', 'fr'),
('TG', 'Togo', 'XOF', 'fr-TG,ee,hna,kbp,dag,ha'),
('TH', 'Thailand', 'THB', 'th,en'),
('TJ', 'Tajikistan', 'TJS', 'tg,ru'),
('TK', 'Tokelau', 'NZD', 'tkl,en-TK'),
('TL', 'East Timor', 'USD', 'tet,pt-TL,id,en'),
('TM', 'Turkmenistan', 'TMT', 'tk,ru,uz'),
('TN', 'Tunisia', 'TND', 'ar-TN,fr'),
('TO', 'Tonga', 'TOP', 'to,en-TO'),
('TR', 'Turkey', 'TRY', 'tr-TR,ku,diq,az,av'),
('TT', 'Trinidad and Tobago', 'TTD', 'en-TT,hns,fr,es,zh'),
('TV', 'Tuvalu', 'AUD', 'tvl,en,sm,gil'),
('TW', 'Taiwan', 'TWD', 'zh-TW,zh,nan,hak'),
('TZ', 'Tanzania', 'TZS', 'sw-TZ,en,ar'),
('UA', 'Ukraine', 'UAH', 'uk,ru-UA,rom,pl,hu'),
('UG', 'Uganda', 'UGX', 'en-UG,lg,sw,ar'),
('UM', 'U.S. Minor Outlying Islands', 'USD', 'en-UM'),
('US', 'United States', 'USD', 'en-US,es-US,haw,fr'),
('UY', 'Uruguay', 'UYU', 'es-UY'),
('UZ', 'Uzbekistan', 'UZS', 'uz,ru,tg'),
('VA', 'Vatican City', 'EUR', 'la,it,fr'),
('VC', 'Saint Vincent and the Grenadines', 'XCD', 'en-VC,fr'),
('VE', 'Venezuela', 'VEF', 'es-VE'),
('VG', 'British Virgin Islands', 'USD', 'en-VG'),
('VI', 'U.S. Virgin Islands', 'USD', 'en-VI'),
('VN', 'Vietnam', 'VND', 'vi,en,fr,zh,km'),
('VU', 'Vanuatu', 'VUV', 'bi,en-VU,fr-VU'),
('WF', 'Wallis and Futuna', 'XPF', 'wls,fud,fr-WF'),
('WS', 'Samoa', 'WST', 'sm,en-WS'),
('XK', 'Kosovo', 'EUR', 'sq,sr'),
('YE', 'Yemen', 'YER', 'ar-YE'),
('YT', 'Mayotte', 'EUR', 'fr-YT'),
('ZA', 'South Africa', 'ZAR', 'zu,xh,af,nso,en-ZA,tn'),
('ZM', 'Zambia', 'ZMW', 'en-ZM,bem,loz,lun,lue,ny,toi'),
('ZW', 'Zimbabwe', 'ZWL', 'en-ZW,sn,nr,nd');


ALTER TABLE `edexer_edexer`.`address` 
ADD COLUMN `street1` VARCHAR(80) NULL AFTER `business_card_business_card_id`,
ADD COLUMN `street2` VARCHAR(80) NULL AFTER `street1`,
ADD COLUMN `city` VARCHAR(50) NULL AFTER `street2`,
ADD COLUMN `state` VARCHAR(50) NULL AFTER `city`,
ADD COLUMN `zip` VARCHAR(20) NULL AFTER `state`,
ADD COLUMN `country` INT(5) NULL AFTER `zip`,
ADD INDEX `fk_address_country_idx` (`country` ASC);
ALTER TABLE `edexer_edexer`.`address` 
ADD CONSTRAINT `fk_address_country`
  FOREIGN KEY (`country`)
  REFERENCES `edexer_edexer`.`countries` (`idCountry`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
  
  
ALTER TABLE `edexer_edexer`.`business_card` 
ADD COLUMN `subscription_user_id` INT UNSIGNED NULL AFTER `creator`,
ADD COLUMN `subscription_sub_type` INT UNSIGNED NULL AFTER `subscription_user_id`;

alter table edexer_edexer.business_card add  
  INDEX `fk_user_sub_bc` (`subscription_user_id` ASC, `subscription_sub_type` ASC);
  
  alter table edexer_edexer.business_card add  
  CONSTRAINT `fk_bc_user_sub`
    FOREIGN KEY (`subscription_user_id` , `subscription_sub_type`)
    REFERENCES `edexer_edexer`.`user_subscription` (`user_id` , `sub_type`)
    ON DELETE CASCADE
    ON UPDATE CASCADE;


-- TEST DATA
INSERT INTO `edexer_edexer`.`role` (`role_id`, `role_name`, `role_desc`) VALUES ('1', 'Admin', 'Staff admin');
INSERT INTO `edexer_edexer`.`role` (`role_id`, `role_name`, `role_desc`) VALUES ('2', 'User', 'Normal User, can be either user or subc. admin');


INSERT INTO `edexer_edexer`.`subscription` (`subscription_id`, `subscription_type_name`, `desc`) VALUES ('1', 'Coporate', '');

INSERT INTO `act_as` VALUES (1,'Admin','1'),(2,'Normal User','users other than admin'),(3,'Owner',NULL);


ALTER TABLE `edexer_edexer`.`attachment` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`attachment_id`);

ALTER TABLE `edexer_edexer`.`attachment` 
CHANGE COLUMN `desc` `description` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `edexer_edexer`.`user_subscription` ADD COLUMN `admin_user` INT UNSIGNED NULL  AFTER `note` ;
ALTER TABLE `edexer_edexer`.`user_subscription` 
  ADD CONSTRAINT `fk_user_subscription_subscription_admin`
  FOREIGN KEY (`admin_user` )
  REFERENCES `edexer_edexer`.`user` (`user_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

 ALTER TABLE `edexer_edexer`.`user_subscription` ADD COLUMN `subscription_owner` INT(10) UNSIGNED NULL  AFTER `admin_user` , 
  ADD CONSTRAINT `fk_user_subscription_subscription_owner`
  FOREIGN KEY (`subscription_owner` )
  REFERENCES `edexer_edexer`.`user` (`user_id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_user_subscription_subscription_owner_idx` (`subscription_owner` ASC) ;

  
ALTER TABLE `edexer_edexer`.`user_subscription` 
DROP FOREIGN KEY `fk_user_subscription_subscription1`;
ALTER TABLE `edexer_edexer`.`user_subscription` 
ADD CONSTRAINT `fk_user_subscription_subscription1`
  FOREIGN KEY (`sub_type`)
  REFERENCES `edexer_edexer`.`subscription` (`subscription_id`)
  ON DELETE RESTRICT
  ON UPDATE CASCADE;

  
  ALTER TABLE `edexer_edexer`.`bc_permissions` 
DROP FOREIGN KEY `fk_bc_permissions_user_subscription1`;
ALTER TABLE `edexer_edexer`.`bc_permissions` 
ADD CONSTRAINT `fk_bc_permissions_user_subscription1`
  FOREIGN KEY (`user_subscription_user_id` , `user_subscription_sub_type`)
  REFERENCES `edexer_edexer`.`user_subscription` (`user_id` , `sub_type`)
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

ALTER TABLE `edexer_edexer`.`business_card` 
CHANGE COLUMN `subscription_user_id` `subscription_user_id` INT(10) UNSIGNED NULL DEFAULT NULL AFTER `bc_last_name`,
CHANGE COLUMN `subscription_sub_type` `subscription_sub_type` INT(10) UNSIGNED NULL DEFAULT NULL AFTER `subscription_user_id`,
ADD COLUMN `avatar` VARCHAR(200) NULL AFTER `creator`;


INSERT INTO `sector` VALUES (1,'Accident & Health Insurance\r',NULL),(2,'Advertising Agencies\r',NULL),(5,'Aerospace/Defense - Major Diversified\r',NULL),(6,'Aerospace/Defense Products & Services\r',NULL),(7,'Agricultural Chemicals\r',NULL),(8,'Air Delivery & Freight Services\r',NULL),(9,'Air Services',NULL),(10,'Aluminum\r',NULL),(11,'Apparel Stores\r',NULL),(12,'Appliances\r',NULL),(13,'Application Software\r',NULL),(14,'Asset Management\r',NULL),(15,'Auto Dealerships\r',NULL),(16,'Auto Manufacturers - Major\r',NULL),(17,'Auto Parts\r',NULL),(18,'Auto Parts Stores\r',NULL),(19,'Auto Parts Wholesale\r',NULL),(20,'Basic Materials Wholesale\r',NULL),(21,'Beverages - Brewers\r',NULL),(22,'Beverages - Soft Drinks\r',NULL),(23,'Beverages - Wineries & Distillers\r',NULL),(24,'Biotechnology\r',NULL),(25,'Broadcasting - Radio\r',NULL),(26,'Broadcasting - TV\r',NULL),(27,'Building Materials Wholesale\r',NULL),(28,'Business Equipment\r',NULL),(29,'Business Services\r',NULL),(30,'Business Software & Services\r',NULL),(31,'Catalog & Mail Order Houses\r',NULL),(32,'CATV Systems\r',NULL),(33,'Cement\r',NULL),(34,'Chemicals - Major Diversified\r',NULL),(35,'Cigarettes\r',NULL),(36,'Cleaning Products\r',NULL),(37,'Closed-End Fund - Debt\r',NULL),(38,'Closed-End Fund - Equity\r',NULL),(39,'Closed-End Fund - Foreign\r',NULL),(40,'Communication Equipment\r',NULL),(41,'Computer Based Systems\r',NULL),(42,'Computer Peripherals\r',NULL),(43,'Computers Wholesale\r',NULL),(44,'Confectioners\r',NULL),(45,'Conglomerates\r',NULL),(46,'Consumer Services\r',NULL),(47,'Copper\r',NULL),(48,'Credit Services\r',NULL),(49,'Dairy Products\r',NULL),(50,'Data Storage Devices\r',NULL),(51,'Department Stores\r',NULL),(52,'Diagnostic Substances\r',NULL),(53,'Discount',NULL),(54,'Diversified Communication Services\r',NULL),(55,'Diversified Computer Systems\r',NULL),(56,'Diversified Electronics\r',NULL),(57,'Diversified Investments\r',NULL),(58,'Diversified Machinery\r',NULL),(59,'Diversified Utilities\r',NULL),(60,'Drug Delivery\r',NULL),(61,'Drug Manufacturers - Major\r',NULL),(62,'Drug Manufacturers - Other\r',NULL),(63,'Drug Related Products\r',NULL),(64,'Drug Stores\r',NULL),(65,'Drugs - Generic\r',NULL),(66,'Drugs Wholesale\r',NULL),(67,'Education & Training Services\r',NULL),(68,'Electric Utilities\r',NULL),(69,'Electronic Equipment\r',NULL),(70,'Electronics Stores\r',NULL),(71,'Electronics Wholesale\r',NULL),(72,'Entertainment - Diversified\r',NULL),(73,'Farm & Construction Machinery\r',NULL),(74,'Farm Products\r',NULL),(75,'Food - Major Diversified\r',NULL),(76,'Food Wholesale\r',NULL),(77,'Foreign Money Center Banks\r',NULL),(78,'Foreign Regional Banks\r',NULL),(79,'Foreign Utilities\r',NULL),(80,'Gaming Activities\r',NULL),(81,'Gas Utilities\r',NULL),(82,'General Building Materials\r',NULL),(83,'General Contractors\r',NULL),(84,'General Entertainment\r',NULL),(85,'Gold\r',NULL),(86,'Grocery Stores\r',NULL),(87,'Health Care Plans\r',NULL),(88,'Healthcare Information Services\r',NULL),(89,'Heavy Construction\r',NULL),(90,'Home Furnishing Stores\r',NULL),(91,'Home Furnishings & Fixtures\r',NULL),(92,'Home Health Care\r',NULL),(93,'Home Improvement Stores\r',NULL),(94,'Hospitals\r',NULL),(95,'Housewares & Accessories\r',NULL),(96,'Independent Oil & Gas\r',NULL),(97,'Industrial Electrical Equipment\r',NULL),(98,'Industrial Equipment & Components\r',NULL),(99,'Industrial Equipment Wholesale\r',NULL),(100,'Industrial Metals & Minerals\r',NULL),(101,'Insurance Brokers\r',NULL),(102,'Internet Information Providers\r',NULL),(103,'Internet Service Providers\r',NULL),(104,'Internet Software & Services\r',NULL),(105,'Investment Brokerage - National\r',NULL),(106,'Investment Brokerage - Regional\r',NULL),(107,'Jewelry Stores\r',NULL),(108,'Life Insurance\r',NULL),(109,'Lodging\r',NULL),(110,'Long Distance Carriers\r',NULL),(111,'Long-Term Care Facilities\r',NULL),(112,'\"Lumber',NULL),(113,'Machine Tools & Accessories\r',NULL),(114,'Major Airlines\r',NULL),(115,'Major Integrated Oil & Gas\r',NULL),(116,'Management Services\r',NULL),(117,'Manufactured Housing\r',NULL),(118,'Marketing Services\r',NULL),(119,'Meat Products\r',NULL),(120,'Medical Appliances & Equipment\r',NULL),(121,'Medical Equipment Wholesale\r',NULL),(122,'Medical Instruments & Supplies\r',NULL),(123,'Medical Laboratories & Research\r',NULL),(124,'Medical Practitioners\r',NULL),(125,'Metal Fabrication\r',NULL),(126,'Money Center Banks\r',NULL),(127,'Mortgage Investment\r',NULL),(128,'\"Movie Production',NULL),(129,'Multimedia & Graphics Software\r',NULL),(130,'Music & Video Stores\r',NULL),(131,'Networking & Communication Devices\r',NULL),(132,'Nonmetallic Mineral Mining\r',NULL),(133,'Office Supplies\r',NULL),(134,'Oil & Gas Drilling & Exploration\r',NULL),(135,'Oil & Gas Equipment & Services\r',NULL),(136,'Oil & Gas Pipelines\r',NULL),(137,'Oil & Gas Refining & Marketing\r',NULL),(138,'Packaging & Containers\r',NULL),(139,'Paper & Paper Products\r',NULL),(140,'Personal Computers\r',NULL),(141,'Personal Products\r',NULL),(142,'Personal Services\r',NULL),(143,'Photographic Equipment & Supplies\r',NULL),(144,'Pollution & Treatment Controls\r',NULL),(145,'Printed Circuit Boards\r',NULL),(146,'Processed & Packaged Goods\r',NULL),(147,'Processing Systems & Products\r',NULL),(148,'Property & Casualty Insurance\r',NULL),(149,'Property Management\r',NULL),(150,'Publishing - Books\r',NULL),(151,'Publishing - Newspapers\r',NULL),(152,'Publishing - Periodicals\r',NULL),(153,'Railroads\r',NULL),(154,'Real Estate Development\r',NULL),(155,'\"Recreational Goods',NULL),(156,'Recreational Vehicles\r',NULL),(157,'Regional - Mid-Atlantic Banks\r',NULL),(158,'Regional - Midwest Banks\r',NULL),(159,'Regional - Northeast Banks\r',NULL),(160,'Regional - Pacific Banks\r',NULL),(161,'Regional - Southeast Banks\r',NULL),(162,'Regional - Southwest  Banks\r',NULL),(163,'Regional Airlines\r',NULL),(164,'REIT - Diversified\r',NULL),(165,'REIT - Healthcare Facilities\r',NULL),(166,'REIT - Hotel/Motel\r',NULL),(167,'REIT - Industrial\r',NULL),(168,'REIT - Office\r',NULL),(169,'REIT - Residential\r',NULL),(170,'REIT - Retail\r',NULL),(171,'Rental & Leasing Services\r',NULL),(172,'Research Services\r',NULL),(173,'Residential Construction\r',NULL),(174,'Resorts & Casinos\r',NULL),(175,'Restaurants\r',NULL),(176,'Rubber & Plastics\r',NULL),(177,'Savings & Loans\r',NULL),(178,'Scientific & Technical Instruments\r',NULL),(179,'Security & Protection Services\r',NULL),(180,'Security Software & Services\r',NULL),(181,'Semiconductor - Broad Line\r',NULL),(182,'Semiconductor - Integrated Circuits\r',NULL),(183,'Semiconductor - Specialized\r',NULL),(184,'Semiconductor Equipment & Materials\r',NULL),(185,'Semiconductor- Memory Chips\r',NULL),(186,'Shipping\r',NULL),(187,'Silver\r',NULL),(188,'Small Tools & Accessories\r',NULL),(189,'Specialized Health Services\r',NULL),(190,'Specialty Chemicals\r',NULL),(191,'Specialty Eateries\r',NULL),(192,'\"Specialty Retail',NULL),(193,'Sporting Activities\r',NULL),(194,'Sporting Goods\r',NULL),(195,'Sporting Goods Stores\r',NULL),(196,'Staffing & Outsourcing Services\r',NULL),(197,'Steel & Iron\r',NULL),(198,'Surety & Title Insurance\r',NULL),(199,'Synthetics\r',NULL),(200,'Technical & System Software\r',NULL),(201,'Technical Services\r',NULL),(202,'Telecom Services - Domestic\r',NULL),(203,'Telecom Services - Foreign\r',NULL),(204,'Textile - Apparel Clothing\r',NULL),(205,'Textile - Apparel Footwear & Accessories\r',NULL),(206,'Textile Industrial\r',NULL),(207,'Tobacco Products',NULL),(208,'Toy & Hobby Stores\r',NULL),(209,'Toys & Games\r',NULL),(210,'Trucking\r',NULL),(211,'Trucks & Other Vehicles\r',NULL),(212,'Waste Management\r',NULL),(213,'Water Utilities\r',NULL),(214,'Wholesale',NULL),(215,'Wireless Communications\r',NULL),(216,'Information & Delivery Services\r',NULL),(217,'Information Technology Services\r',NULL);
  
ALTER TABLE `edexer_edexer`.`user_subscription` 
ADD COLUMN `can_export` VARCHAR(1) NULL AFTER `subscription_owner`;

ALTER TABLE `edexer_edexer`.`user_subscription` 
DROP FOREIGN KEY `fk_user_subscription_subscription_admin`;
ALTER TABLE `edexer_edexer`.`user_subscription` 
CHANGE COLUMN `subscription_owner` `subscription_owner` INT(10) UNSIGNED NULL DEFAULT NULL AFTER `note`,
CHANGE COLUMN `admin_user` `isAdmin` VARCHAR(1) NULL DEFAULT 'N' ,
DROP INDEX `fk_user_subscription_subscription_admin` ;
-- Change user-role to role 
ALTER TABLE `edexer_edexer`.`user` 
DROP FOREIGN KEY `FK_user_role_id`;
ALTER TABLE `edexer_edexer`.`user` 
CHANGE COLUMN `user_role` `roleid` INT(10) UNSIGNED NULL DEFAULT NULL ;
ALTER TABLE `edexer_edexer`.`user` 
ADD CONSTRAINT `FK_role_id`
  FOREIGN KEY (`roleid`)
  REFERENCES `edexer_edexer`.`role` (`role_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  ALTER TABLE `edexer_edexer`.`user` 
DROP INDEX `FK_user_role_id_idx` ,
ADD INDEX `FK_role_id_idx` (`roleid` ASC);

-- add User status table
CREATE TABLE `edexer_edexer`.`user_status` (
  `user_status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(50) NOT NULL,
  `status_desc` VARCHAR(50) NULL,
  PRIMARY KEY (`user_status_id`))
ENGINE = InnoDB;

-- Drop Table user_role
DROP TABLE `edexer_edexer`.`user_role`;
-- Change user subscription (Last_edit_by) from not null to null  
  ALTER TABLE `edexer_edexer`.`user_subscription` 
DROP FOREIGN KEY `fk_user_subscription_User2`;
ALTER TABLE `edexer_edexer`.`user_subscription` 
CHANGE COLUMN `last_edit_by` `last_edit_by` INT(10) UNSIGNED NULL ;
ALTER TABLE `edexer_edexer`.`user_subscription` 
ADD CONSTRAINT `fk_user_subscription_User2`
  FOREIGN KEY (`last_edit_by`)
  REFERENCES `edexer_edexer`.`user` (`user_id`);

  ALTER TABLE `edexer_edexer`.`user` 
ADD COLUMN `status` INT(10) UNSIGNED NULL AFTER `password`,
ADD INDEX `FK_status_id_idx` (`status` ASC);
ALTER TABLE `edexer_edexer`.`user` 
ADD CONSTRAINT `FK_status_id`
  FOREIGN KEY (`status`)
  REFERENCES `edexer_edexer`.`user_status` (`user_status_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

INSERT INTO `edexer_edexer`.`user_status` (`status`) VALUES ('ACTIVE');
INSERT INTO `edexer_edexer`.`user_status` (`status`) VALUES ('DELETED');
INSERT INTO `edexer_edexer`.`user_status` (`status`) VALUES ('SUSPENDED');
INSERT INTO `edexer_edexer`.`user_status` (`status`) VALUES ('BLOCKED');
INSERT INTO `social_networks_types` VALUES (1,'facebook',NULL),(2,'twitter',NULL);
INSERT INTO `subscription_status` VALUES (1,'Active',NULL),(2,'Suspended',NULL),(3,'Blocked',NULL),(4,'Ended',NULL),(5,'Pending',NULL);


ALTER TABLE `edexer_edexer`.`user` 
ADD COLUMN `rememberme` VARCHAR(45) NULL AFTER `activation_str`;

CREATE TABLE `edexer_edexer`.`password_reset` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(100) NULL,
  `creation_time` DATETIME NOT NULL,
  `user_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `password_reset_user_idx` (`user_id` ASC),
  CONSTRAINT `password_reset_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `edexer_edexer`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

ALTER TABLE `edexer_edexer`.`password_reset` 
ADD COLUMN `used` VARCHAR(45) NULL DEFAULT 'N' AFTER `user_id`;

ALTER TABLE `edexer_edexer`.`user` 
ADD COLUMN `salt` VARCHAR(50) NULL AFTER `password`;


insert into admin_config values ('WORKBOOK_UPLOAD_MAX',5);
insert into admin_config values ('Regstration_State','OPENED');


  
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


CREATE TABLE IF NOT EXISTS `edexer_edexer`.`mail_config`
(
`mail_config_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`user_id` INT UNSIGNED NOT NULL UNIQUE,
`from_email` VARCHAR(50) NOT NULL,
`from_email_password` VARCHAR(50) NOT NULL,
`encryption_type` VARCHAR(50) NOT NULL,
`outgoing_mail_server` VARCHAR(50) NOT NULL,
`port_number` INT NOT NULL,
`authentication` boolean NULL,
`time_out` int Null,
PRIMARY KEY (`mail_config_id`),
CONSTRAINT `fk_email_config_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `edexer_edexer`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE 
)ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `edexer_edexer`.`privacy_level`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`privacy_level` (
  `privacy_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `privacy_name` VARCHAR(50) NULL,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`privacy_id`))
ENGINE = InnoDB;
-- ----------------------------------------------------
-- -Add Column privacy_id related with table privacy_level
-- ----------------------------------------------------
ALTER TABLE `edexer_edexer`.`user` 
ADD COLUMN `privacy_level_id` INT(10) UNSIGNED NULL AFTER `password`,
ADD INDEX `FK_privacy_level_idx` (`privacy_level_id` ASC);
ALTER TABLE `edexer_edexer`.`user` 
ADD CONSTRAINT `fk_user_privacy`
  FOREIGN KEY (`privacy_level_id`)
  REFERENCES `edexer_edexer`.`privacy_level` (`privacy_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
-- -----------------------------------------------------
-- Table `edexer_edexer`.`bc_request_status`-------------------
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS `edexer_edexer`.`bc_request_status` (
  `status_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `status_name` VARCHAR(50) NULL,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;

-- ------------------------------------------------------
-- -Table `edexer_edexer`.`BC_Request`--------------------------
-- ------------------------------------------------------
CREATE TABLE `bc_request` (
  `bc_request_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_user_id` int(11) unsigned NOT NULL,
  `reciver_user_id` int(11) unsigned NOT NULL,
  `send_date` datetime DEFAULT NULL,
  `message` text,
  `req_status_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`bc_request_id`),
  UNIQUE KEY `reciver_user_id_UNIQUE` (`reciver_user_id`),
  KEY `FK_sender_user_idx` (`sender_user_id`),
  KEY `fk_req_status_id_idx` (`req_status_id`),
  CONSTRAINT `fk_reciver_user_id` FOREIGN KEY (`reciver_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_req_status_id` FOREIGN KEY (`req_status_id`) REFERENCES `bc_request_status` (`status_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sender_user_id` FOREIGN KEY (`sender_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- -Insert request Status Types-------------------------
-- ------------------------------------------------------
INSERT INTO `edexer_edexer`.`bc_request_status` (`status_name`, `desc`) VALUES ('Accept', 'accept user request');
INSERT INTO `edexer_edexer`.`bc_request_status` (`status_name`, `desc`) VALUES ('Ignore', 'refuse user request');
-- -Insert Privacy Level Types--------------------------
-- ------------------------------------------------------
INSERT INTO `edexer_edexer`.`privacy_level` (`privacy_name`, `desc`) VALUES ('Public', 'show all personal data for any user');
INSERT INTO `edexer_edexer`.`privacy_level` (`privacy_name`, `desc`) VALUES ('Private', 'hide all personal data from any user');
INSERT INTO `edexer_edexer`.`privacy_level` (`privacy_name`) VALUES ('Protected');


INSERT INTO `edexer_edexer`.`user` (`user_email`, `first_name`, `last_name`, `password`, `salt`, `status`, `roleid`) VALUES ('admin@findon.com', 'Web', 'Master', '07f423331cfbf492819d7f15ee33880af2b75e9675b6d07d', '83aca74aa19ed370a71f9dbff86d054724fae9ad87d60547', '1', '1');
INSERT INTO `edexer_edexer`.`user_subscription` (`user_id`, `sub_type`, `start_date`, `act_as`, `sub_status`, `subscription_owner`, `isAdmin`, `can_export`) VALUES ('1', '1', '2015-01-01', '1', '1', '1', 'Y', 'Y');



ALTER TABLE `edexer_edexer`.`title` 
DROP FOREIGN KEY `fk_title_sector1`;
ALTER TABLE `edexer_edexer`.`title` 
CHANGE COLUMN `sector_sector_id` `sector_sector_id` INT(10) UNSIGNED NULL ;
ALTER TABLE `edexer_edexer`.`title` 
ADD CONSTRAINT `fk_title_sector1`
  FOREIGN KEY (`sector_sector_id`)
  REFERENCES `edexer_edexer`.`sector` (`sector_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;








use edexer_edexer;
drop procedure if exists `advancedFilter`;

delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `advancedFilter`(
		 IN page_size int
		,IN offsetCount int
		,IN user_id_param int
        ,IN sub_type_param int
        ,IN first_name_param varchar(50) 
        ,IN last_name_param varchar(50)
        ,IN title_param varchar(100)
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
DECLARE sub_type_free int(10) DEFAULT 3;
DECLARE sub_type_pro int(10) DEFAULT 2;
DECLARE sub_type_corp int(10) DEFAULT 1;


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
    edexer_edexer.user_subscription AS us4user,edexer_edexer.business_card AS bc
        LEFT JOIN
    edexer_edexer.user_subscription AS us ON bc.subscription_sub_type = us.sub_type
        AND bc.subscription_user_id = us.user_id
        LEFT JOIN
        edexer_edexer.bc_permissions AS bcpermission ON bc.business_card_id = bcpermission.bc_id
        LEFT JOIN
    edexer_edexer.address AS address ON bc.business_card_id = address.business_card_business_card_id
        LEFT JOIN
    edexer_edexer.countries AS countries ON address.country = countries.idCountry
        LEFT JOIN
    edexer_edexer.bc_tags AS tags ON bc.business_card_id = tags.bc_id
        LEFT JOIN
    edexer_edexer.email AS email ON bc.business_card_id = email.bc_id
        LEFT JOIN
    edexer_edexer.mobile mob ON bc.business_card_id = mob.bc_id
        LEFT JOIN
    edexer_edexer.title AS title ON bc.business_card_id = title.bc_id
        LEFT JOIN
    edexer_edexer.sector AS sector ON title.sector_sector_id = sector.sector_id
WHERE
us4user.user_id=user_id_param
AND
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
        WHEN (bc.subscription_sub_type = sub_type_corp && us4user.isAdmin!='Y' )
        THEN bcpermission.user_subscription_user_id = user_id_param
        ELSE 1
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





use edexer_edexer;
drop procedure if exists `advancedFilterRowCount`;

delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `advancedFilterRowCount`(
		 IN page_size int
		,IN offsetCount int
		,IN user_id_param int
        ,IN sub_type_param int
        ,IN first_name_param varchar(50) 
        ,IN last_name_param varchar(50)
        ,IN title_param varchar(100)
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
 
DECLARE sub_type_free int(10) DEFAULT 3;
DECLARE sub_type_pro int(10) DEFAULT 2;
DECLARE sub_type_corp int(10) DEFAULT 1;
SELECT COUNT(distinct(bc.business_card_id))
FROM
    edexer_edexer.user_subscription AS us4user, edexer_edexer.business_card AS bc
        LEFT JOIN
    edexer_edexer.user_subscription AS us ON bc.subscription_sub_type = us.sub_type
        AND bc.subscription_user_id = us.user_id
        LEFT JOIN
        edexer_edexer.bc_permissions AS bcpermission ON bc.business_card_id = bcpermission.bc_id
        LEFT JOIN
    edexer_edexer.address AS address ON bc.business_card_id = address.business_card_business_card_id
        LEFT JOIN
    edexer_edexer.countries AS countries ON address.country = countries.idCountry
        LEFT JOIN
    edexer_edexer.bc_tags AS tags ON bc.business_card_id = tags.bc_id
        LEFT JOIN
    edexer_edexer.email AS email ON bc.business_card_id = email.bc_id
        LEFT JOIN
    edexer_edexer.mobile mob ON bc.business_card_id = mob.bc_id
        LEFT JOIN
    edexer_edexer.title AS title ON bc.business_card_id = title.bc_id
        LEFT JOIN
    edexer_edexer.sector AS sector ON title.sector_sector_id = sector.sector_id
WHERE
us4user.user_id=user_id_param
AND
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
        WHEN (bc.subscription_sub_type = sub_type_corp && us4user.isAdmin!="Y")
        THEN bcpermission.user_subscription_user_id = user_id_param
        ELSE 1
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
;


 END//
 delimiter ;






use edexer_edexer;
drop procedure if exists `getUserSubscription`;
delimiter //
CREATE PROCEDURE getUserSubscription(IN userId int,IN subType int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_type = subType ;
 END //
 delimiter ;



 drop procedure if exists `getUserSubscriptionsByUserId`;
delimiter //
CREATE PROCEDURE getUserSubscriptionsByUserId(IN userId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId;
 END //
 delimiter ;
 
 
 drop procedure if exists `getUserSubscriptionsByUserIdAndState`;
 delimiter //
CREATE PROCEDURE getUserSubscriptionsByUserIdAndState(IN userId int, IN statusId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_status = statusId ;
 END //
 delimiter ;
 
 
 
 drop procedure if exists `getUserSubscriptionsByStatus`;
  delimiter //
CREATE PROCEDURE getUserSubscriptionsByStatus(IN statusId int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.sub_status = statusId ;
 END //
 delimiter ;
 
 
 
 drop procedure if exists `getUserSubscriptionByUserIdAndType`;
  delimiter //
CREATE PROCEDURE getUserSubscriptionByUserIdAndType(IN userId int, IN subscriptionType int)
 BEGIN
 SELECT * 
 FROM user_subscription
 WHERE user_subscription.user_id = userId and user_subscription.sub_type = subscriptionType ;
 END //
 delimiter ;
 
 
 
 drop procedure if exists `getTagsByUserSubscription`;
   delimiter //
CREATE PROCEDURE getTagsByUserSubscription(IN user_id int, IN sub_type int)
 BEGIN
 SELECT * 
 FROM tags
 WHERE tags.user_id = user_id and tags.sub_type = sub_type ;
 END //
 delimiter ;
 
 
 drop procedure if exists `getMobileByIdAndSubscription`;
   delimiter //
CREATE PROCEDURE getMobileByIdAndSubscription(IN mobileNum varchar(20), IN user_id int, IN sub_type int, IN bcId int)
 BEGIN
select * from mobile as m
inner join business_card as bc on m.bc_id = bc.business_card_id
where m.mobile_num like mobileNum
#and bc.subscription_sub_type = sub_type
#and bc.subscription_user_id = user_id
AND (CASE
WHEN sub_type = 0
THEN 1
ELSE (bc.subscription_sub_type = sub_type)
end
)
AND (CASE
WHEN user_id = 0
THEN 1
ELSE (bc.subscription_user_id = user_id)
end
)
AND (CASE
WHEN bcId != 0
THEN (m.bc_id != bcId)
ELSE 1
end
)
;
 END //
 delimiter ;
 
 
  drop procedure if exists `getPhoneByIdAndSubscription`;
   delimiter //
CREATE PROCEDURE getPhoneByIdAndSubscription(IN phoneNum varchar(20), IN user_id int, IN sub_type int, IN bcId int)
 BEGIN
select * from phone as p
inner join business_card as bc on p.bc_id = bc.business_card_id
where p.phone_num like phoneNum
#and bc.subscription_sub_type = sub_type
#and bc.subscription_user_id = user_id
AND (CASE
WHEN sub_type = 0
THEN 1
ELSE (bc.subscription_sub_type = sub_type)
end
)
AND (CASE
WHEN user_id = 0
THEN 1
ELSE (bc.subscription_user_id = user_id)
end
)
AND (CASE
WHEN bcId != 0
THEN (p.bc_id != bcId)
ELSE 1
end
)
;
 END //
 delimiter ;

 
 drop procedure if exists `getFaxByIdAndSubscription`;
   delimiter //
CREATE PROCEDURE getFaxByIdAndSubscription(IN faxNum varchar(20), IN user_id int, IN sub_type int, IN bcId int)
 BEGIN
select * from fax as f
inner join business_card as bc on f.bc_id = bc.business_card_id
where f.fax_num like faxNum
#and bc.subscription_sub_type = sub_type
#and bc.subscription_user_id = user_id
AND (CASE
WHEN sub_type = 0
THEN 1
ELSE (bc.subscription_sub_type = sub_type)
end
)
AND (CASE
WHEN user_id = 0
THEN 1
ELSE (bc.subscription_user_id = user_id)
end
)
AND (CASE
WHEN bcId != 0
THEN (f.bc_id != bcId)
ELSE 1
end
)
;
 END //
 delimiter ;
 
 
 
 drop procedure if exists `getEmailByIdAndSubscription`;
   delimiter //
CREATE PROCEDURE getEmailByIdAndSubscription(IN emailAddress varchar(150), IN user_id int, IN sub_type int, IN bcId int)
 BEGIN
select * from email as e
inner join business_card as bc on e.bc_id = bc.business_card_id
where e.email_address like emailAddress
#and bc.subscription_sub_type = sub_type
#and bc.subscription_user_id = user_id
AND (CASE
WHEN sub_type = 0
THEN 1
ELSE (bc.subscription_sub_type = sub_type)
end
)
AND (CASE
WHEN user_id = 0
THEN 1
ELSE (bc.subscription_user_id = user_id)
end
)
AND (CASE
WHEN bcId != 0
THEN (e.bc_id != bcId)
ELSE 1
end
)
;
 END //
 delimiter ;

 drop procedure if exists `getUsersForCorporateSubscription`;
 delimiter //
CREATE PROCEDURE getUsersForCorporateSubscription(IN ownerId int)
 BEGIN
 select * from user as u
inner join user_subscription as us on u.user_id = us.user_id
where us.subscription_owner = ownerId;
 END //
 delimiter ;
 

 
 use edexer_edexer;
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


use edexer_edexer;
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

use edexer_edexer;
drop procedure if exists `getUsersHasNoPermForThisBC`;
delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUsersHasNoPermForThisBC`(IN bcId int)
BEGIN
select distinct  *  from user_subscription u 
left join bc_permissions p on (u.user_id = p.user_subscription_user_id and u.sub_type=p.user_subscription_sub_type)
where  u.user_id not in (select  perm.user_subscription_user_id from bc_permissions perm where perm.bc_id=bcId );
END//
delimiter ;


use edexer_edexer;
drop procedure if exists `getUsersHasPermForThisBC`;
delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUsersHasPermForThisBC`(IN bcId int)
BEGIN
select distinct  *  from user_subscription u 
where  u.user_id  in (select  perm.user_subscription_user_id from bc_permissions perm where perm.bc_id=bcId );
END//
delimiter ;


