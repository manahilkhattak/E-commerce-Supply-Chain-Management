-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: supply_chain_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `contract_id` bigint NOT NULL AUTO_INCREMENT,
  `contract_number` varchar(50) NOT NULL,
  `contract_title` varchar(255) NOT NULL,
  `contract_type` varchar(50) DEFAULT NULL,
  `contract_value` decimal(15,2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `end_date` date NOT NULL,
  `notes` text,
  `payment_terms` varchar(100) DEFAULT NULL,
  `renewal_terms` text,
  `signed_date` date DEFAULT NULL,
  `start_date` date NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`contract_id`),
  UNIQUE KEY `UKbx9jyu2cccdntb3ehrf0ojpfd` (`contract_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_orders`
--

DROP TABLE IF EXISTS `customer_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_delivery_date` datetime(6) DEFAULT NULL,
  `billing_address` text,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(3) DEFAULT NULL,
  `customer_email` varchar(100) NOT NULL,
  `customer_id` bigint NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_phone` varchar(20) DEFAULT NULL,
  `discount_amount` decimal(8,2) DEFAULT NULL,
  `estimated_delivery_date` datetime(6) DEFAULT NULL,
  `final_amount` decimal(12,2) DEFAULT NULL,
  `internal_notes` text,
  `order_notes` text,
  `order_number` varchar(50) NOT NULL,
  `order_status` varchar(50) NOT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `payment_status` varchar(50) DEFAULT NULL,
  `pick_list_id` bigint DEFAULT NULL,
  `priority_level` varchar(20) DEFAULT NULL,
  `quality_check_id` bigint DEFAULT NULL,
  `shipment_id` bigint DEFAULT NULL,
  `shipping_address` text NOT NULL,
  `shipping_cost` decimal(8,2) DEFAULT NULL,
  `tax_amount` decimal(8,2) DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_id` bigint DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `UKs4wt1sgd48rj6cgahwlksogx` (`order_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_exceptions`
--

DROP TABLE IF EXISTS `delivery_exceptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_exceptions` (
  `exception_id` bigint NOT NULL AUTO_INCREMENT,
  `assigned_to` varchar(100) DEFAULT NULL,
  `carrier` varchar(100) DEFAULT NULL,
  `carrier_contact` varchar(255) DEFAULT NULL,
  `claim_reference` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_contact_date` datetime(6) DEFAULT NULL,
  `customer_contacted` bit(1) DEFAULT NULL,
  `customer_response` varchar(255) DEFAULT NULL,
  `estimated_compensation_amount` double DEFAULT NULL,
  `estimated_resolution_date` datetime(6) DEFAULT NULL,
  `exception_date` datetime(6) NOT NULL,
  `exception_description` text NOT NULL,
  `exception_location` varchar(255) DEFAULT NULL,
  `exception_number` varchar(50) NOT NULL,
  `exception_severity` varchar(20) NOT NULL,
  `exception_status` varchar(50) NOT NULL,
  `exception_type` varchar(50) NOT NULL,
  `insurance_claim_filed` bit(1) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `package_id` bigint DEFAULT NULL,
  `priority_level` varchar(20) DEFAULT NULL,
  `reported_by` varchar(100) DEFAULT NULL,
  `requires_insurance_claim` bit(1) DEFAULT NULL,
  `shipment_id` bigint NOT NULL,
  `tracking_number` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`exception_id`),
  UNIQUE KEY `UKrx9gtdr7y2bk3kqi0a9tee7nd` (`exception_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_status`
--

DROP TABLE IF EXISTS `delivery_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_status` (
  `delivery_status_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_delivery` datetime(6) DEFAULT NULL,
  `carrier` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_status` varchar(50) NOT NULL,
  `customer_notified` bit(1) DEFAULT NULL,
  `delivery_attempts` int DEFAULT NULL,
  `estimated_delivery` datetime(6) DEFAULT NULL,
  `exception_reason` varchar(255) DEFAULT NULL,
  `exception_resolution` varchar(255) DEFAULT NULL,
  `is_delivered` bit(1) DEFAULT NULL,
  `is_exception` bit(1) DEFAULT NULL,
  `last_location` varchar(255) DEFAULT NULL,
  `last_updated` datetime(6) NOT NULL,
  `order_id` bigint NOT NULL,
  `package_id` bigint DEFAULT NULL,
  `service_type` varchar(50) DEFAULT NULL,
  `shipment_id` bigint NOT NULL,
  `signed_by` varchar(100) DEFAULT NULL,
  `status_description` varchar(255) DEFAULT NULL,
  `tracking_number` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`delivery_status_id`),
  UNIQUE KEY `UKai8m4ynk8xjlsjjo5dgex40nq` (`tracking_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `demand_forecasts`
--

DROP TABLE IF EXISTS `demand_forecasts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demand_forecasts` (
  `forecast_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_demand` int DEFAULT NULL,
  `adjusted_demand` int DEFAULT NULL,
  `base_demand` int NOT NULL,
  `confidence_level` decimal(5,2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `end_date` date NOT NULL,
  `forecast_date` date NOT NULL,
  `forecast_error` int DEFAULT NULL,
  `forecast_method` varchar(50) DEFAULT NULL,
  `forecast_period` varchar(20) NOT NULL,
  `forecast_status` varchar(50) DEFAULT NULL,
  `historical_accuracy` decimal(5,2) DEFAULT NULL,
  `mean_absolute_error` decimal(10,2) DEFAULT NULL,
  `notes` text,
  `predicted_demand` int NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `promotion_impact` decimal(5,2) DEFAULT NULL,
  `seasonality_factor` decimal(5,2) DEFAULT NULL,
  `start_date` date NOT NULL,
  `trend_factor` decimal(5,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`forecast_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dispatch_schedules`
--

DROP TABLE IF EXISTS `dispatch_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispatch_schedules` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_date_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `dispatch_status` varchar(50) DEFAULT NULL,
  `driver_name` varchar(100) DEFAULT NULL,
  `notes` text,
  `schedule_type` varchar(50) NOT NULL,
  `scheduled_date_time` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `vehicle_number` varchar(50) DEFAULT NULL,
  `shipment_id` bigint NOT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `FK5510mrdmk2xoofbd295fs33a4` (`shipment_id`),
  CONSTRAINT `FK5510mrdmk2xoofbd295fs33a4` FOREIGN KEY (`shipment_id`) REFERENCES `shipments` (`shipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exception_resolutions`
--

DROP TABLE IF EXISTS `exception_resolutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exception_resolutions` (
  `resolution_id` bigint NOT NULL AUTO_INCREMENT,
  `action_taken` text,
  `additional_notes` text,
  `compensation_amount` double DEFAULT NULL,
  `compensation_approved_by` varchar(100) DEFAULT NULL,
  `cost_incurred` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_satisfaction_rating` int DEFAULT NULL,
  `preventive_measures` text,
  `reshipment_date` datetime(6) DEFAULT NULL,
  `reshipment_tracking_number` varchar(100) DEFAULT NULL,
  `resolution_date` datetime(6) DEFAULT NULL,
  `resolution_description` text NOT NULL,
  `resolution_duration_hours` int DEFAULT NULL,
  `resolution_type` varchar(50) NOT NULL,
  `resolved_by` varchar(100) DEFAULT NULL,
  `root_cause_analysis` text,
  `updated_at` datetime(6) DEFAULT NULL,
  `exception_id` bigint NOT NULL,
  PRIMARY KEY (`resolution_id`),
  UNIQUE KEY `UK7dsftrgh16wqvs58kvebip6or` (`exception_id`),
  CONSTRAINT `FK2pr5o71d9wngn8rp2gpexgskx` FOREIGN KEY (`exception_id`) REFERENCES `delivery_exceptions` (`exception_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods_receipts`
--

DROP TABLE IF EXISTS `goods_receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_receipts` (
  `receipt_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `delivery_note_number` varchar(50) DEFAULT NULL,
  `discrepancy_found` bit(1) DEFAULT NULL,
  `notes` text,
  `po_id` bigint NOT NULL,
  `po_number` varchar(50) DEFAULT NULL,
  `receipt_date` date NOT NULL,
  `receipt_number` varchar(50) NOT NULL,
  `received_by` varchar(100) NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `total_items_ordered` int DEFAULT NULL,
  `total_items_received` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `vehicle_number` varchar(50) DEFAULT NULL,
  `warehouse_location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`receipt_id`),
  UNIQUE KEY `UKmfasptxi0jgu968w81s5f89mg` (`receipt_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inspection_records`
--

DROP TABLE IF EXISTS `inspection_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspection_records` (
  `inspection_id` bigint NOT NULL AUTO_INCREMENT,
  `accepted_quantity` int DEFAULT NULL,
  `action_taken` varchar(100) DEFAULT NULL,
  `batch_number` varchar(50) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `damaged_quantity` int DEFAULT NULL,
  `defect_type` varchar(100) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `inspection_date` datetime(6) DEFAULT NULL,
  `inspection_notes` text,
  `inspection_status` varchar(50) DEFAULT NULL,
  `inspector_name` varchar(100) NOT NULL,
  `ordered_quantity` int NOT NULL,
  `photo_url` varchar(500) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) DEFAULT NULL,
  `quality_rating` int DEFAULT NULL,
  `received_quantity` int NOT NULL,
  `rejected_quantity` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `receipt_id` bigint NOT NULL,
  PRIMARY KEY (`inspection_id`),
  KEY `FKtldj0i6h4bv8fn1sdhqntoec2` (`receipt_id`),
  CONSTRAINT `FKtldj0i6h4bv8fn1sdhqntoec2` FOREIGN KEY (`receipt_id`) REFERENCES `goods_receipts` (`receipt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inventory_discrepancies`
--

DROP TABLE IF EXISTS `inventory_discrepancies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_discrepancies` (
  `discrepancy_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_quantity` int NOT NULL,
  `adjusted_by` varchar(100) DEFAULT NULL,
  `assigned_to` varchar(100) DEFAULT NULL,
  `corrective_action` text,
  `created_at` datetime(6) DEFAULT NULL,
  `discrepancy_category` varchar(50) DEFAULT NULL,
  `discrepancy_severity` varchar(20) DEFAULT NULL,
  `expected_quantity` int NOT NULL,
  `is_adjusted_in_system` bit(1) DEFAULT NULL,
  `location_code` varchar(50) DEFAULT NULL,
  `notes` text,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `resolution_date` datetime(6) DEFAULT NULL,
  `resolution_notes` text,
  `resolution_status` varchar(50) DEFAULT NULL,
  `resolved_by` varchar(100) DEFAULT NULL,
  `root_cause` varchar(255) DEFAULT NULL,
  `shelf_location_id` bigint DEFAULT NULL,
  `system_adjustment_date` datetime(6) DEFAULT NULL,
  `unit_cost` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `variance_quantity` int NOT NULL,
  `variance_type` varchar(255) DEFAULT NULL,
  `variance_value` double NOT NULL,
  `warehouse_id` bigint NOT NULL,
  `report_id` bigint NOT NULL,
  PRIMARY KEY (`discrepancy_id`),
  KEY `FK18i3m4yb71b2mnjibmxbvi42g` (`report_id`),
  CONSTRAINT `FK18i3m4yb71b2mnjibmxbvi42g` FOREIGN KEY (`report_id`) REFERENCES `reconciliation_reports` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inventory_monitoring`
--

DROP TABLE IF EXISTS `inventory_monitoring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_monitoring` (
  `inventory_id` bigint NOT NULL AUTO_INCREMENT,
  `available_stock` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_stock` int NOT NULL,
  `days_of_supply` int DEFAULT NULL,
  `is_monitored` bit(1) DEFAULT NULL,
  `last_restocked_date` datetime(6) DEFAULT NULL,
  `last_sold_date` datetime(6) DEFAULT NULL,
  `maximum_stock_level` int DEFAULT NULL,
  `minimum_stock_level` int NOT NULL,
  `movement_frequency` varchar(50) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `reorder_point` int NOT NULL,
  `reserved_stock` int DEFAULT NULL,
  `stock_status` varchar(50) DEFAULT NULL,
  `stock_turnover_rate` double DEFAULT NULL,
  `stock_value` double DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  UNIQUE KEY `UK9e2fmvsojltuk1aiwbgv0d9yj` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `brand` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `inventory_id` bigint DEFAULT NULL,
  `is_fragile` bit(1) DEFAULT NULL,
  `item_notes` text,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `quantity` int NOT NULL,
  `requires_quality_check` bit(1) DEFAULT NULL,
  `shelf_location_id` bigint DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `weight_kg` decimal(8,3) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKb2vrrqy10nnyqhb5ergl5498r` (`order_id`),
  CONSTRAINT `FKb2vrrqy10nnyqhb5ergl5498r` FOREIGN KEY (`order_id`) REFERENCES `customer_orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_items`
--

DROP TABLE IF EXISTS `package_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `package_items` (
  `package_item_id` bigint NOT NULL AUTO_INCREMENT,
  `is_fragile` bit(1) DEFAULT NULL,
  `item_notes` text,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `quantity` int NOT NULL,
  `requires_special_handling` bit(1) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `weight_kg` decimal(8,2) NOT NULL,
  `package_id` bigint NOT NULL,
  PRIMARY KEY (`package_item_id`),
  KEY `FKlk7ccw8ywkjidktb4627y7vbj` (`package_id`),
  CONSTRAINT `FKlk7ccw8ywkjidktb4627y7vbj` FOREIGN KEY (`package_id`) REFERENCES `packages` (`package_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `package_id` bigint NOT NULL AUTO_INCREMENT,
  `carrier` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customs_declaration_required` bit(1) DEFAULT NULL,
  `dimensions` varchar(100) DEFAULT NULL,
  `insurance_amount` decimal(10,2) DEFAULT NULL,
  `is_fragile` bit(1) DEFAULT NULL,
  `is_hazardous` bit(1) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `package_notes` text,
  `package_size` varchar(50) DEFAULT NULL,
  `package_status` varchar(50) DEFAULT NULL,
  `package_type` varchar(50) DEFAULT NULL,
  `packed_at` datetime(6) DEFAULT NULL,
  `packed_by` varchar(100) DEFAULT NULL,
  `pick_list_id` bigint NOT NULL,
  `requires_signature` bit(1) DEFAULT NULL,
  `service_type` varchar(100) DEFAULT NULL,
  `shipping_cost` decimal(10,2) DEFAULT NULL,
  `temperature_control` varchar(50) DEFAULT NULL,
  `tracking_number` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `weight_kg` decimal(8,2) DEFAULT NULL,
  PRIMARY KEY (`package_id`),
  UNIQUE KEY `UKeoq3w5c9d428h0jlkr2h9wa47` (`tracking_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pick_list_items`
--

DROP TABLE IF EXISTS `pick_list_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pick_list_items` (
  `pick_item_id` bigint NOT NULL AUTO_INCREMENT,
  `aisle_number` varchar(10) DEFAULT NULL,
  `is_picked` bit(1) DEFAULT NULL,
  `location_code` varchar(50) NOT NULL,
  `pick_notes` text,
  `pick_sequence` int DEFAULT NULL,
  `picked_at` datetime(6) DEFAULT NULL,
  `picked_quantity` int DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `required_quantity` int NOT NULL,
  `shelf_location_id` bigint NOT NULL,
  `weight_per_unit_kg` double DEFAULT NULL,
  `zone_code` varchar(20) DEFAULT NULL,
  `pick_list_id` bigint NOT NULL,
  PRIMARY KEY (`pick_item_id`),
  KEY `FKbrvpvej8nqb0qkgimtxwqibx6` (`pick_list_id`),
  CONSTRAINT `FKbrvpvej8nqb0qkgimtxwqibx6` FOREIGN KEY (`pick_list_id`) REFERENCES `pick_lists` (`pick_list_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pick_lists`
--

DROP TABLE IF EXISTS `pick_lists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pick_lists` (
  `pick_list_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_pick_time_minutes` int DEFAULT NULL,
  `assigned_to` varchar(100) DEFAULT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `estimated_pick_time_minutes` int DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `pick_list_number` varchar(50) NOT NULL,
  `pick_notes` text,
  `pick_route_optimized` bit(1) DEFAULT NULL,
  `pick_status` varchar(50) DEFAULT NULL,
  `picked_items` int DEFAULT NULL,
  `priority_level` varchar(20) DEFAULT NULL,
  `remaining_items` int DEFAULT NULL,
  `started_at` datetime(6) DEFAULT NULL,
  `total_items` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `warehouse_name` varchar(255) DEFAULT NULL,
  `zone_sequence` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pick_list_id`),
  UNIQUE KEY `UK7ipth6a44n677nqe4oyq8ik5a` (`pick_list_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` bigint NOT NULL AUTO_INCREMENT,
  `barcode` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  `cost_price` decimal(38,2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_stock` int NOT NULL,
  `description` text,
  `dimensions` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `last_stock_update` datetime(6) DEFAULT NULL,
  `maximum_stock_level` int DEFAULT NULL,
  `minimum_stock_level` int NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(255) NOT NULL,
  `reorder_point` int DEFAULT NULL,
  `selling_price` decimal(38,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `unit_of_measurement` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UKifvg6qjjgj4ie5k7oacs02rdw` (`product_sku`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_order_items`
--

DROP TABLE IF EXISTS `purchase_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_items` (
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount_rate` decimal(5,2) DEFAULT NULL,
  `line_total` decimal(15,2) DEFAULT NULL,
  `notes` text,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) DEFAULT NULL,
  `quantity` int NOT NULL,
  `received_quantity` int DEFAULT NULL,
  `tax_rate` decimal(5,2) DEFAULT NULL,
  `unit_of_measurement` varchar(20) DEFAULT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `po_id` bigint NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `FK5y0w29ahv8gqn5hq6ug5f9u9o` (`po_id`),
  CONSTRAINT `FK5y0w29ahv8gqn5hq6ug5f9u9o` FOREIGN KEY (`po_id`) REFERENCES `purchase_orders` (`po_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `po_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_delivery_date` date DEFAULT NULL,
  `approval_date` date DEFAULT NULL,
  `approved_by` varchar(100) DEFAULT NULL,
  `contract_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `delivery_address` text,
  `discount_amount` decimal(15,2) DEFAULT NULL,
  `expected_delivery_date` date DEFAULT NULL,
  `final_amount` decimal(15,2) DEFAULT NULL,
  `notes` text,
  `order_date` date NOT NULL,
  `payment_terms` varchar(100) DEFAULT NULL,
  `po_number` varchar(50) NOT NULL,
  `requested_by` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `tax_amount` decimal(15,2) DEFAULT NULL,
  `total_amount` decimal(15,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`po_id`),
  UNIQUE KEY `UKpbiykvcpyg0jslne4gviyeuc2` (`po_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_checks`
--

DROP TABLE IF EXISTS `quality_checks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_checks` (
  `check_id` bigint NOT NULL AUTO_INCREMENT,
  `approved_for_shipment` bit(1) DEFAULT NULL,
  `are_labels_correct` bit(1) DEFAULT NULL,
  `check_notes` text,
  `check_number` varchar(50) NOT NULL,
  `check_status` varchar(50) DEFAULT NULL,
  `check_type` varchar(50) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `content_accuracy_score` int DEFAULT NULL,
  `corrective_actions` text,
  `created_at` datetime(6) DEFAULT NULL,
  `inspector_name` varchar(100) NOT NULL,
  `is_content_correct` bit(1) DEFAULT NULL,
  `is_hazardous_compliant` bit(1) DEFAULT NULL,
  `is_package_damaged` bit(1) DEFAULT NULL,
  `is_weight_accurate` bit(1) DEFAULT NULL,
  `issues_found` text,
  `label_accuracy_score` int DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `overall_result` varchar(20) DEFAULT NULL,
  `package_id` bigint NOT NULL,
  `package_integrity_score` int DEFAULT NULL,
  `recheck_notes` text,
  `recheck_required` bit(1) DEFAULT NULL,
  `safety_compliance_score` int DEFAULT NULL,
  `score_percentage` double DEFAULT NULL,
  `started_at` datetime(6) DEFAULT NULL,
  `tracking_number` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `weight_accuracy_score` int DEFAULT NULL,
  PRIMARY KEY (`check_id`),
  UNIQUE KEY `UKkic0lewmeohwnk85c8iuupmqb` (`check_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_standards`
--

DROP TABLE IF EXISTS `quality_standards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quality_standards` (
  `standard_id` bigint NOT NULL AUTO_INCREMENT,
  `check_type` varchar(50) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `dimension_tolerance_percentage` double DEFAULT NULL,
  `effective_date` datetime(6) DEFAULT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `labeling_requirements` text,
  `minimum_score` int NOT NULL,
  `notes` text,
  `packaging_requirements` text,
  `product_category` varchar(100) DEFAULT NULL,
  `required_documentation` text,
  `safety_requirements` text,
  `standard_code` varchar(50) NOT NULL,
  `standard_name` varchar(255) NOT NULL,
  `target_score` int NOT NULL,
  `testing_procedures` text,
  `updated_at` datetime(6) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL,
  `weight_tolerance_percentage` double DEFAULT NULL,
  PRIMARY KEY (`standard_id`),
  UNIQUE KEY `UKlpc6k3sl5lxtvjl1thcnhsswn` (`standard_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reconciliation_reports`
--

DROP TABLE IF EXISTS `reconciliation_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reconciliation_reports` (
  `report_id` bigint NOT NULL AUTO_INCREMENT,
  `accuracy_rate` double DEFAULT NULL,
  `approval_status` varchar(50) DEFAULT NULL,
  `approved_by` varchar(100) DEFAULT NULL,
  `approved_date` datetime(6) DEFAULT NULL,
  `conducted_by` varchar(100) DEFAULT NULL,
  `conducted_date` datetime(6) NOT NULL,
  `corrective_actions` text,
  `created_at` datetime(6) DEFAULT NULL,
  `discrepancy_value` double DEFAULT NULL,
  `notes` text,
  `preventive_measures` text,
  `report_number` varchar(50) NOT NULL,
  `report_period_end` datetime(6) NOT NULL,
  `report_period_start` datetime(6) NOT NULL,
  `report_status` varchar(50) NOT NULL,
  `report_type` varchar(50) NOT NULL,
  `reviewed_by` varchar(100) DEFAULT NULL,
  `reviewed_date` datetime(6) DEFAULT NULL,
  `summary_findings` text,
  `total_actual_quantity` int DEFAULT NULL,
  `total_discrepancies_found` int DEFAULT NULL,
  `total_expected_quantity` int DEFAULT NULL,
  `total_products_counted` int DEFAULT NULL,
  `total_sku_counted` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `variance_rate` double DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `warehouse_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  UNIQUE KEY `UK24k4ygmd2mlubfpyx3kcf6jqk` (`report_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reorder_plans`
--

DROP TABLE IF EXISTS `reorder_plans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reorder_plans` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT,
  `calculated_service_level` decimal(5,2) DEFAULT NULL,
  `conversion_date` datetime(6) DEFAULT NULL,
  `converted_to_po` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `current_stock` int NOT NULL,
  `daily_demand_rate` int NOT NULL,
  `economic_order_quantity` int DEFAULT NULL,
  `estimated_cost` decimal(15,2) DEFAULT NULL,
  `expected_delivery_date` date DEFAULT NULL,
  `expected_stockout_date` date DEFAULT NULL,
  `forecast_id` bigint DEFAULT NULL,
  `lead_time_days` int NOT NULL,
  `notes` text,
  `order_urgency` varchar(50) DEFAULT NULL,
  `plan_status` varchar(50) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `purchase_order_id` bigint DEFAULT NULL,
  `recommended_order_quantity` int NOT NULL,
  `reorder_point` int NOT NULL,
  `safety_stock` int NOT NULL,
  `service_level_target` decimal(5,2) DEFAULT NULL,
  `stockout_risk_level` varchar(20) DEFAULT NULL,
  `suggested_order_date` date NOT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`plan_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `restock_records`
--

DROP TABLE IF EXISTS `restock_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restock_records` (
  `restock_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `current_value` double DEFAULT NULL,
  `is_sellable` bit(1) DEFAULT NULL,
  `item_condition` varchar(50) DEFAULT NULL,
  `location_code` varchar(50) DEFAULT NULL,
  `original_cost` double DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `quality_grade` varchar(20) DEFAULT NULL,
  `repair_notes` text,
  `requires_repair` bit(1) DEFAULT NULL,
  `restock_date` datetime(6) NOT NULL,
  `restock_notes` text,
  `restock_quantity` int NOT NULL,
  `restocked_by` varchar(100) DEFAULT NULL,
  `sellable_quantity` int DEFAULT NULL,
  `shelf_location_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `value_adjustment_reason` varchar(255) DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  `return_order_id` bigint NOT NULL,
  PRIMARY KEY (`restock_id`),
  KEY `FKetj7p82d6v97q1t6sxmbrxv7x` (`return_order_id`),
  CONSTRAINT `FKetj7p82d6v97q1t6sxmbrxv7x` FOREIGN KEY (`return_order_id`) REFERENCES `return_orders` (`return_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `return_items`
--

DROP TABLE IF EXISTS `return_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_items` (
  `return_item_id` bigint NOT NULL AUTO_INCREMENT,
  `is_restockable` bit(1) DEFAULT NULL,
  `item_condition` varchar(50) DEFAULT NULL,
  `original_order_item_id` bigint NOT NULL,
  `original_quantity` int NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `quality_notes` text,
  `restock_quantity` int DEFAULT NULL,
  `return_quantity` int NOT NULL,
  `return_reason` varchar(100) DEFAULT NULL,
  `unit_price` double NOT NULL,
  `return_order_id` bigint NOT NULL,
  PRIMARY KEY (`return_item_id`),
  KEY `FKtqlrdkarnsanj3537huxt6xy6` (`return_order_id`),
  CONSTRAINT `FKtqlrdkarnsanj3537huxt6xy6` FOREIGN KEY (`return_order_id`) REFERENCES `return_orders` (`return_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `return_orders`
--

DROP TABLE IF EXISTS `return_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_orders` (
  `return_order_id` bigint NOT NULL AUTO_INCREMENT,
  `approval_date` datetime(6) DEFAULT NULL,
  `approved_by` varchar(100) DEFAULT NULL,
  `carrier_for_return` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `customer_comments` text,
  `customer_email` varchar(100) DEFAULT NULL,
  `customer_id` bigint NOT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `exchange_order_id` bigint DEFAULT NULL,
  `is_restockable` bit(1) DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `pickup_completed_date` datetime(6) DEFAULT NULL,
  `pickup_required` bit(1) DEFAULT NULL,
  `pickup_scheduled_date` datetime(6) DEFAULT NULL,
  `quality_check_notes` text,
  `quality_grade` varchar(20) DEFAULT NULL,
  `refund_amount` double DEFAULT NULL,
  `refund_method` varchar(50) DEFAULT NULL,
  `refund_status` varchar(50) DEFAULT NULL,
  `rejection_reason` text,
  `request_date` datetime(6) NOT NULL,
  `resolution_notes` text,
  `restocking_fee` double DEFAULT NULL,
  `return_description` text,
  `return_number` varchar(50) NOT NULL,
  `return_reason` varchar(100) NOT NULL,
  `return_status` varchar(50) NOT NULL,
  `return_tracking_number` varchar(100) DEFAULT NULL,
  `return_type` varchar(50) NOT NULL,
  `shipping_cost_refund` double DEFAULT NULL,
  `store_credit_amount` double DEFAULT NULL,
  `total_refund_amount` double DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warehouse_id` bigint DEFAULT NULL,
  PRIMARY KEY (`return_order_id`),
  UNIQUE KEY `UK5g0dsl0dlw8ynte8dogx354f5` (`return_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shelf_locations`
--

DROP TABLE IF EXISTS `shelf_locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shelf_locations` (
  `shelf_id` bigint NOT NULL AUTO_INCREMENT,
  `aisle_number` varchar(10) NOT NULL,
  `available_units` int DEFAULT NULL,
  `bin_number` varchar(10) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_units` int DEFAULT NULL,
  `current_weight_kg` double DEFAULT NULL,
  `dimensions` varchar(100) DEFAULT NULL,
  `is_occupied` bit(1) DEFAULT NULL,
  `last_picked` datetime(6) DEFAULT NULL,
  `last_restocked` datetime(6) DEFAULT NULL,
  `level_number` varchar(10) NOT NULL,
  `location_code` varchar(50) NOT NULL,
  `location_status` varchar(50) DEFAULT NULL,
  `location_type` varchar(50) DEFAULT NULL,
  `max_capacity_units` int DEFAULT NULL,
  `max_weight_kg` double DEFAULT NULL,
  `notes` text,
  `occupancy_rate` double DEFAULT NULL,
  `pick_frequency` int DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_sku` varchar(50) DEFAULT NULL,
  `shelf_number` varchar(10) NOT NULL,
  `temperature_requirement` varchar(50) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `zone_id` bigint DEFAULT NULL,
  `warehouse_id` bigint NOT NULL,
  PRIMARY KEY (`shelf_id`),
  UNIQUE KEY `UK6559yu5lbisf4ahu2ajj4qr0u` (`location_code`),
  KEY `FKsc1a4ittjs1i2fbflw78vcq4e` (`zone_id`),
  KEY `FK9x68lk8qou76aytqy3sh7hop9` (`warehouse_id`),
  CONSTRAINT `FK9x68lk8qou76aytqy3sh7hop9` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`warehouse_id`),
  CONSTRAINT `FKsc1a4ittjs1i2fbflw78vcq4e` FOREIGN KEY (`zone_id`) REFERENCES `storage_zones` (`zone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shipments`
--

DROP TABLE IF EXISTS `shipments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipments` (
  `shipment_id` bigint NOT NULL AUTO_INCREMENT,
  `actual_delivery_date` date DEFAULT NULL,
  `carrier` varchar(100) NOT NULL,
  `carrier_tracking_url` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `destination_address` text NOT NULL,
  `estimated_delivery_date` date DEFAULT NULL,
  `insurance_amount` decimal(10,2) DEFAULT NULL,
  `is_fragile` bit(1) DEFAULT NULL,
  `is_insured` bit(1) DEFAULT NULL,
  `notes` text,
  `order_id` bigint NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `origin_address` text,
  `package_dimensions` varchar(100) DEFAULT NULL,
  `package_id` bigint NOT NULL,
  `package_weight_kg` decimal(8,2) DEFAULT NULL,
  `pickup_date` date DEFAULT NULL,
  `recipient_email` varchar(100) DEFAULT NULL,
  `recipient_name` varchar(255) NOT NULL,
  `recipient_phone` varchar(20) DEFAULT NULL,
  `requires_signature` bit(1) DEFAULT NULL,
  `service_type` varchar(100) NOT NULL,
  `shipment_date` date DEFAULT NULL,
  `shipment_status` varchar(50) DEFAULT NULL,
  `shipping_cost` decimal(10,2) DEFAULT NULL,
  `special_instructions` text,
  `tracking_number` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`shipment_id`),
  UNIQUE KEY `UK2980t5kjkkrwnjhwvit59x61k` (`tracking_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slas`
--

DROP TABLE IF EXISTS `slas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slas` (
  `sla_id` bigint NOT NULL AUTO_INCREMENT,
  `compliance_percentage` double DEFAULT NULL,
  `contract_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `measurement_unit` varchar(50) DEFAULT NULL,
  `metric_description` text,
  `metric_name` varchar(255) NOT NULL,
  `minimum_acceptable` varchar(100) DEFAULT NULL,
  `monitoring_frequency` varchar(50) DEFAULT NULL,
  `penalty_clause` text,
  `status` varchar(50) DEFAULT NULL,
  `target_value` varchar(100) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`sla_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_alerts`
--

DROP TABLE IF EXISTS `stock_alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_alerts` (
  `alert_id` bigint NOT NULL AUTO_INCREMENT,
  `alert_level` varchar(20) NOT NULL,
  `alert_type` varchar(50) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_stock` int DEFAULT NULL,
  `inventory_id` bigint NOT NULL,
  `is_resolved` bit(1) DEFAULT NULL,
  `message` text NOT NULL,
  `notification_sent` bit(1) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_sku` varchar(50) NOT NULL,
  `resolution_notes` text,
  `resolved_at` datetime(6) DEFAULT NULL,
  `resolved_by` varchar(100) DEFAULT NULL,
  `suggested_action` text,
  `threshold_stock` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`alert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `storage_zones`
--

DROP TABLE IF EXISTS `storage_zones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_zones` (
  `zone_id` bigint NOT NULL AUTO_INCREMENT,
  `access_requirements` varchar(100) DEFAULT NULL,
  `aisle_count` int DEFAULT NULL,
  `available_capacity_sqft` double DEFAULT NULL,
  `capacity_utilization` double DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `current_weight_kg` double DEFAULT NULL,
  `max_weight_capacity_kg` double DEFAULT NULL,
  `notes` text,
  `shelf_count` int DEFAULT NULL,
  `temperature_control` varchar(50) DEFAULT NULL,
  `total_capacity_sqft` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `used_capacity_sqft` double DEFAULT NULL,
  `zone_code` varchar(20) NOT NULL,
  `zone_name` varchar(255) NOT NULL,
  `zone_status` varchar(50) DEFAULT NULL,
  `zone_type` varchar(50) NOT NULL,
  `warehouse_id` bigint NOT NULL,
  PRIMARY KEY (`zone_id`),
  KEY `FK1g9w3niy8spb6kn8mayuqkiuc` (`warehouse_id`),
  CONSTRAINT `FK1g9w3niy8spb6kn8mayuqkiuc` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_id` bigint NOT NULL AUTO_INCREMENT,
  `address` text,
  `approved_at` datetime(6) DEFAULT NULL,
  `business_license` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `company_name` varchar(255) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `notes` text,
  `payment_terms` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `registered_at` datetime(6) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `tax_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `UKq5uvp89ra4ksaty5ghyaw4kjr` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tracking_events`
--

DROP TABLE IF EXISTS `tracking_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tracking_events` (
  `tracking_event_id` bigint NOT NULL AUTO_INCREMENT,
  `carrier` varchar(100) DEFAULT NULL,
  `carrier_status_code` varchar(50) DEFAULT NULL,
  `carrier_status_description` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `delivery_notes` text,
  `estimated_delivery` datetime(6) DEFAULT NULL,
  `event_description` varchar(255) NOT NULL,
  `event_location` varchar(255) DEFAULT NULL,
  `event_timestamp` datetime(6) NOT NULL,
  `event_type` varchar(50) NOT NULL,
  `is_milestone` bit(1) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `order_id` bigint NOT NULL,
  `package_id` bigint DEFAULT NULL,
  `shipment_id` bigint NOT NULL,
  `signed_by` varchar(100) DEFAULT NULL,
  `tracking_number` varchar(100) NOT NULL,
  PRIMARY KEY (`tracking_event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `warehouses`
--

DROP TABLE IF EXISTS `warehouses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouses` (
  `warehouse_id` bigint NOT NULL AUTO_INCREMENT,
  `address` text NOT NULL,
  `available_capacity_sqft` double DEFAULT NULL,
  `capacity_utilization` double DEFAULT NULL,
  `city` varchar(100) NOT NULL,
  `contact_email` varchar(100) DEFAULT NULL,
  `contact_phone` varchar(20) DEFAULT NULL,
  `country` varchar(100) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `manager_name` varchar(100) DEFAULT NULL,
  `notes` text,
  `occupied_shelves` int DEFAULT NULL,
  `operating_hours` varchar(100) DEFAULT NULL,
  `postal_code` varchar(20) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `temperature_zone` varchar(50) DEFAULT NULL,
  `total_capacity_sqft` double NOT NULL,
  `total_shelves` int DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `used_capacity_sqft` double DEFAULT NULL,
  `warehouse_code` varchar(20) NOT NULL,
  `warehouse_name` varchar(255) NOT NULL,
  `warehouse_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `UK1bidj95gewy1e3q54xbdrxykv` (`warehouse_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-12 21:26:41
