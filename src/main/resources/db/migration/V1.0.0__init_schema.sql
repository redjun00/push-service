CREATE TABLE device (
  `id` INT NOT NULL,
  `device_token` BIGINT(20) NOT NULL,
  `started_at` DATETIME NULL,
  PRIMARY KEY (`id`, `device_token`),
  UNIQUE INDEX `device_token_UNIQUE` (`device_token` ASC)
);