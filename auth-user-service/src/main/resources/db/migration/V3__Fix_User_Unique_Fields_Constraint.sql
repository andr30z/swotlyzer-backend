ALTER TABLE "users" DROP  CONSTRAINT unique_phone_email_users;
ALTER TABLE "users" ADD CONSTRAINT unique_email_users  UNIQUE (email);