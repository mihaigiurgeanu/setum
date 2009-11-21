-- Adaugare camp rata schimb valutar la facturi si plati
ALTER TABLE  `INVOICE_` ADD  `exchangeRate_` DOUBLE NULL DEFAULT  '1';
ALTER TABLE  `PAYMENT_` ADD  `exchangeRate_` DOUBLE NULL DEFAULT  '1';
