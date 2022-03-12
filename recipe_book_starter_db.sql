CREATE DATABASE IF NOT EXISTS `recipe_book_db`;
USE `recipe_book_db`;
--
-- Table structure for table `accounttable`
--
DROP TABLE IF EXISTS `accounttable`;
CREATE TABLE `accounttable` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userName` (`userName`)
) AUTO_INCREMENT=1000;

--
-- Table structure for table `cuisine`
--

DROP TABLE IF EXISTS `cuisine`;
CREATE TABLE `cuisine` (
  `cuisineID` int(11) NOT NULL AUTO_INCREMENT,
  `cuisineName` varchar(20) NOT NULL,
  PRIMARY KEY (`cuisineID`),
  UNIQUE KEY `cuisineName` (`cuisineName`)
);

--
-- Dumping data for table `cuisine`
--

LOCK TABLES `cuisine` WRITE;
/*!40000 ALTER TABLE `cuisine` DISABLE KEYS */;
INSERT INTO `cuisine` VALUES 
(1,'Chinese'), (2,'Asian'), (3,'French'), (4,'British'), (5,'English'),
(6,'Scottish'), (7,'European'), (8,'Mediterranean'), (9,'Italian'), (10,'Indian'),
(11,'Mexican'), (12,'Middle Eastern'), (13,'Greek'), (14,'Japanese'), (15,'Southern') , (16,'American');
/*!40000 ALTER TABLE `cuisine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredient`
--

DROP TABLE IF EXISTS `ingredient`;
CREATE TABLE `ingredient` (
  `ingredientID` int(11) NOT NULL AUTO_INCREMENT,
  `ingredientName` varchar(50) NOT NULL,
  PRIMARY KEY (`ingredientID`),
  UNIQUE KEY `ingredientName` (`ingredientName`)
); 


--
-- Dumping data for table `ingredient`
--

LOCK TABLES `ingredient` WRITE;
/*!40000 ALTER TABLE `ingredient` DISABLE KEYS */;
INSERT INTO `ingredient` VALUES 
(19912,'agave nectar'),(93607,'almond milk'),(11120426,'apple cider vinegar'),(1032035,'asafetida'),(11120524,'asiago cheese'),
(9037,'avocado'),(10123,'bacon'),(18371,'baking powder'),(10012224,'balck pepper'),(11120458,'bamboo shoots'),(9040,'bananas'),
(11120545,'basil leaves'),(10211821,'bell pepper'),(11120467,'black eyed peas'),(10012222,'black pepper'),(11120425,'blackberries'),
(11120492,'blueberries'),(5062,'bonless chicken breasts'),(11120504,'bourbon'),(98840,'broccolini'),(11120521,'brown rice syrup'),
(19334,'brown sugar'),(10012221,'Butter'),(11485,'butternut squash'),(11120435,'button mushrooms'),(16018,'canned black beans'),
(11120535,'canned pumpkin puree'),(10011693,'canned tomatoes'),(11120444,'canola oil'),(2031,'cayenne'),(11120436,'cherry tomatoes'),
(6194,'chicken broth'),(11120456,'chili flakes'),(2009,'chili powder'),(11120477,'chillies'),(11120538,'chocolate shavings'),(2010,'cinnamon'),
(11120478,'cinnamon powder'),(11120431,'clear honey'),(11120497,'condensed milk'),(11120421,'cooked penne'),(20137,'cooked quinoa'),
(11120496,'corn syrup'),(11120452,'courgettes'),(11120485,'crackers'),(11120508,'cream cheese'),(11120514,'crème fraîche'),(11206,'cucumber'),
(1002014,'cumin'),(11120479,'cumin seeds'),(11120480,'curry leaves'),(11120468,'curry powder'),(11120511,'deep dish pie shell'),(11120464,'dijon mustard'),
(2045,'dill'),(2003,'dried basil'),(16056,'dried chickpeas'),(9079,'dried cranberries'),(11120424,'dry white wine'),(11120422,'duck breasts'),(1123,'egg'),
(11120500,'egg yolks'),(11209,'eggplant'),(11120486,'eggs'),(6599,'enchilada sauce'),(1019,'feta cheese'),(11120546,'flaxseed oil'),(10012220,'flaxseeds'),
(11120550,'french bread'),(2044,'fresh basil'),(11156,'fresh chives'),(11165,'fresh cilantro'),(11216,'fresh ginger'),(1026,'fresh mozzarella'),(11297,'fresh parsley'),
(11120423,'fresh rosemary'),(2041,'fresh tarragon'),(11120463,'fresh thyme leaves'),(11120509,'fudge ice cream topping'),(11120469,'garam masala'),(11215,'garlic cloves'),
(11120470,'globe eggplant'),(1159,'goat cheese'),(11120519,'golden syrup'),(11120493,'graham cracker crumbs'),(11120533,'graham crackers'),(11120502,'granulated sugar'),
(11120542,'grapefruit'),(11333,'green bell peppers'),(11291,'green onion tops'),(11120450,'green onions'),(10023572,'ground beef'),(11120454,'ground coriander'),(11120460,'ground cumin'),
(11120428,'ground nutmeg'),(1002030,'ground pepper'),(1049,'half & half'),(11120525,'half and half'),(11120462,'halloumi cheese'),(11120540,'hazelnuts'),(6168,'hot sauce'),
(11120553,'hummus'),(11120539,'instant lemon pudding mix'),(11120523,'italian sausage'),(1022027,'italian seasoning'),(11120472,'juice of lemon'),(11120554,'kalamata olives'),
(11233,'kale'),(1082047,'kosher salt'),(10620420,'lasagna pasta'),(9152,'lemon juice'),(11120548,'lemon peel'),(11120494,'lemon zest'),(11120455,'lemons'),
(11120438,'light coconut milk'),(11120522,'light muscovado sugar'),(11120432,'light soy sauce'),(11120441,'lime juice'),(11120501,'lime zest'),(11120547,'low fat cottage cheese'),
(11120499,'maple sugar'),(11120503,'maple syrup'),(10111549,'marinara sauce'),(11120517,'mascarpone cheese'),(11120530,'mayonnaise'),(11120526,'mild cheddar cheese'),(11120498,'minute tapioca'),
(11120505,'molasses'),(11120481,'mung beans'),(11120471,'mustard powder'),(11120482,'mustard seeds'),(11120534,'nutella'),(11120495,'nutter butter cookies'),(4053,'olive oil'),(11282,'onion'),
(10011821,'orange bell pepper'),(11120440,'orange juice'),(11120429,'orange zest'),(2027,'oregano'),(11120532,'oreo cookies'),(11120487,'packaged no-boil lasagna noodles'),(11120527,'parmesan cheese'),
(11120528,'parsley flakes'),(11120507,'peanut butter'),(11120488,'pecans'),(10012223,'pepper'),(11120516,'pie crust'),(11120490,'pie shell'),(11120443,'pineapple chunks'),(11120552,'pita breads'),
(11120465,'pitta bread'),(11120489,'plain flour'),(1001256,'plain greek yogurt'),(11120448,'plain yogurt'),(23612,'pot roast'),(18337,'puff pastry'),(11120537,'pumpkin pie spice'),(20035,'quinoa'),
(11120491,'real bacon recipe pieces'),(11821,'red bell pepper'),(11120473,'red chilies'),(11120427,'red currant jelly'),(11120461,'red onion'),(1032009,'red pepper flakes'),(11120437,'red peppers'),
(1022068,'red wine vinegar'),(20444,'rice'),(1022053,'rice vinegar'),(1036,'ricotta cheese'),(8120,'rolled oats'),(10211529,'roma tomato'),(99226,'sage'),(15076,'salmon'),(11120457,'salmon fillet'),
(2047,'salt'),(11120474,'sea salt'),(1012047,'sea-salt'),(11120475,'shallots'),(11120555,'sharp cheddar'),(11120510,'shortcrust pastry'),(1011026,'shredded cheese'),
(1001026,'shredded mozzarella cheese'),(11120442,'shrimp'),(1055062,'skinless boneless chicken breast'),(11120434,'skinless boneless chicken breast fillets'),
(11120446,'skinless boneless chicken breasts'),(11120459,'smoked paprika'),(11120551,'smoked salmon'),(16124,'soy sauce'),(10011549,'spaghetti sauce'),(11120529,'spinach'),(11120430,'spring onions'),
(11120453,'steaks'),(19335,'sugar'),(11120483,'sunflower oil'),(11168,'sweet corn'),(11120466,'sweet onion'),(11120451,'sweet paprika'),(11120541,'sweetened condensed milk'),(11120476,'swiss chard'),
(11120439,'tabasco sauce'),(11120512,'tart apples'),(11120445,'tbsp olive oil'),(11120447,'thyme'),(11120549,'toast'),(11887,'tomato paste'),(11529,'tomatoes'),(11120513,'turkey'),(11120484,'turmeric'),
(11120520,'vanilla essence'),(2050,'vanilla extract'),(11120506,'vanilla pudding mix'),(2051,'vanilla soy milk'),(11120433,'vegetable oil'),(6615,'vegetable stock'),(11120518,'vine ripened tomatoes'),
(12155,'walnuts'),(14412,'water'),(11120536,'whipped topping'),(11120531,'whipping cream'),(2032,'white pepper'),(11120556,'whole berry cranberry sauce'),(11120515,'whole cranberry sauce'),
(10511282,'yellow onion'),(11477,'zucchini');
/*!40000 ALTER TABLE `ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe`
--

DROP TABLE IF EXISTS `recipe`;

CREATE TABLE `recipe` (
  `recipeID` int(11) NOT NULL AUTO_INCREMENT,
  `recipeName` varchar(100) NOT NULL,
  `cookTime` tinyint(4) DEFAULT NULL,
  `servings` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`recipeID`),
  UNIQUE KEY `recipeName` (`recipeName`)
);

--
-- Dumping data for table `recipe`
--

LOCK TABLES `recipe` WRITE;
/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
INSERT INTO `recipe` VALUES (61761,'No-Bake Nutella Cheesecakes',15,4),(84206,'Praline Cream Cake',45,10),(222545,'Spicy chicken kebabs',45,20),(224732,'American pecan pies',65,6),
(245775,'Smoky Paprika Shrimp Skewers',48,4),(245916,'Grilled Tuna Kebabs',95,4),(246995,'Rosemary Chicken Skewers with Berry Sauce',100,4),(247337,'Salmon Teriyaki Skewers with Pineapple',45,4),
(247362,'Smoked Salmon and Goat Cheese Toasts',30,40),(263078,'Eggnog',45,1),(509198,'Suzanne\'s Breakfast Tacos',15,2),(593195,'Watermelon & Avocado Bruschetta',5,24),
(633692,'Baked Oatmeal with Dried Cranberries',45,9),(633754,'Baked Ratatouille',45,1),(640693,'Creamy Ratatouille Over Penne',45,2),
(658523,'Roasted Butternut Squash Lasanga With Goat Cheese, Bacon, and Fried Sage',45,4),(659109,'Salmon Quinoa Risotto',45,4),(715769,'Broccolini Quinoa Pilaf',30,2),
(716444,'scrambled egg',5,1),(716446,'boiled egg',12,5),(716627,'Easy Homemade Rice and Beans',35,2),(719503,'Caprese Avocado Toast',5,1),
(798400,'Spicy Black-Eyed Pea Curry with Swiss Chard and Roasted Eggplant',45,6);
/*!40000 ALTER TABLE `recipe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_cuisine`
--

DROP TABLE IF EXISTS `recipe_cuisine`;
CREATE TABLE `recipe_cuisine` (
  `recipeID` int(11) NOT NULL,
  `cuisineID` int(11) NOT NULL,
  PRIMARY KEY (`recipeID`,`cuisineID`),
  KEY `fk_recipe_cuisine_cuisine` (`cuisineID`),
  CONSTRAINT `fk_recipe_cuisine_cuisine` FOREIGN KEY (`cuisineID`) REFERENCES `cuisine` (`cuisineID`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipe_cuisine_recipe` FOREIGN KEY (`recipeID`) REFERENCES `recipe` (`recipeID`) ON DELETE CASCADE
);

--
-- Dumping data for table `recipe_cuisine`
--

LOCK TABLES `recipe_cuisine` WRITE;
/*!40000 ALTER TABLE `recipe_cuisine` DISABLE KEYS */;
INSERT INTO `recipe_cuisine` VALUES 
(247337,2),(798400,2),(633754,3),(640693,3),(593195,7),(633754,7),(640693,7),(659109,7),(715769,7),(593195,8),(633754,8),
(640693,8),(659109,8),(715769,8),(593195,9),(659109,9),(715769,9),(798400,10),(509198,11),(247337,14),(224732,16);
/*!40000 ALTER TABLE `recipe_cuisine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_ingredients`
--

DROP TABLE IF EXISTS `recipe_ingredients`;
CREATE TABLE `recipe_ingredients` (
  `recipeID` int(11) NOT NULL,
  `ingredientID` int(11) NOT NULL,
  `measurementID` int(11) NOT NULL,
  `amount` float NOT NULL,
  `form` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`recipeID`,`ingredientID`,`measurementID`),
  KEY `fk_recipe_ingredients_measurement` (`measurementID`),
  KEY `fk_recipe_ingredients_ingredient` (`ingredientID`),
  CONSTRAINT `fk_recipe_ingredients_ingredient` FOREIGN KEY (`ingredientID`) REFERENCES `ingredient` (`ingredientID`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipe_ingredients_measurement` FOREIGN KEY (`measurementID`) REFERENCES `standard_measurement` (`measurementID`),
  CONSTRAINT `fk_recipe_ingredients_recipe` FOREIGN KEY (`recipeID`) REFERENCES `recipe` (`recipeID`) ON DELETE CASCADE
);

--
-- Dumping data for table `recipe_ingredients`
--

LOCK TABLES `recipe_ingredients` WRITE;
/*!40000 ALTER TABLE `recipe_ingredients` DISABLE KEYS */;
INSERT INTO `recipe_ingredients` VALUES 
(61761,2050,10,1,'pure'),(61761,10012221,5,3,'unsalted melted'),(61761,11120508,18,8,'softened'),(61761,11120532,1,12,'crushed'),(61761,11120534,3,0.666667,''),(61761,11120536,18,8,'frozen thawed'),(61761,11120538,11,1,''),(61761,11120540,11,1,'toasted chopped'),
(84206,11120488,38,1,''),(84206,11120508,22,8,'sour'),
(222545,11215,1,3,'roughly chopped'),(222545,11216,32,1,'fresh roughly chopped'),(222545,11120429,1,1,'grated'),(222545,11120430,1,3,'roughly chopped'),(222545,11120431,5,2,''),(222545,11120432,5,1,'light'),(222545,11120433,5,2,''),(222545,11120434,20,4,'boneless skinless cut into cubes'),(222545,11120435,1,20,''),(222545,11120436,1,20,''),(222545,11120437,8,2,'red seeded cut into 10'),
(224732,19334,35,50,''),(224732,10012221,35,150,'melted'),(224732,11120486,8,4,''),(224732,11120488,35,175,''),(224732,11120489,35,300,'plain'),(224732,11120504,5,2,''),(224732,11120519,35,85,''),(224732,11120520,4,1,''),(224732,11120522,35,85,'light'),(224732,11120531,2,150,''),
(245775,11215,1,2,'minced'),(245775,1082047,10,0.5,''),(245775,10012222,10,0.5,'black freshly ground'),(245775,11120441,5,2,''),(245775,11120442,24,1,'cleaned peeled'),(245775,11120451,5,2,'sweet'),(245775,11120458,11,1,''),(245775,11120459,5,2,'smoked'),(245775,11120460,10,0.5,''),
(245916,11215,1,2,''),(245916,11291,31,2,'chopped'),(245916,11333,1,1,'green'),(245916,11821,1,1,'red'),(245916,1082047,10,1,''),(245916,10012222,10,0.5,'black'),(245916,11120423,31,1,'fresh chopped'),(245916,11120435,8,6,''),(245916,11120453,28,1.5,''),(245916,11120455,1,2,'cut into wedges'),(245916,11120466,20,1,'sweet ( if available)'),
(246995,10012224,10,1,''),(246995,11120422,28,1.5,'boneless , skin on'),(246995,11120423,5,2,'fresh chopped'),(246995,11120424,3,0.25,'dry white'),(246995,11120425,3,1.75,'fresh'),(246995,11120426,31,1,''),(246995,11120427,5,2,'red (or berry jam or jelly)'),(246995,11120428,10,0.25,''),
(247337,11215,5,2,'minced'),(247337,11216,5,2,'fresh minced'),(247337,19334,3,0.25,''),(247337,1022053,3,0.25,''),(247337,11120432,3,0.5,''),(247337,11120433,5,2,''),(247337,11120443,34,1,'fresh cut into 1 to 1 1/2-inch chunks'),(247337,11120450,1,2,'cut into 1-inch segments'),(247337,11120450,3,0.25,'minced'),(247337,11120456,21,1,''),(247337,11120457,34,1,'rinsed cut into 1 to 1 1/2-inch cubes'),(247337,11120458,1,8,'for at least 20 minutes before using'),
(247362,1159,15,8,'fresh soft'),(247362,1002030,10,0.5,'black'),(247362,11120423,31,1,'fresh chopped'),(247362,11120463,31,1,'fresh chopped'),(247362,11120548,4,2,'grated'),(247362,11120550,27,30,'thin'),(247362,11120551,15,12,'smoked thinly sliced'),
(263078,2010,4,0.25,''),(263078,2051,3,0.75,'fat-free soymilk'),(263078,9040,1,1,'whole frozen (peeled)'),
(509198,1019,5,2,''),(509198,9037,1,0.25,'diced'),(509198,1012047,16,2,'freshly ground'),(509198,10011693,1,0.5,'diced'),(509198,11120486,1,3,''),
(593195,1019,5,3,'crumbled'),(593195,9037,1,1,'diced pitted ripe finely'),(593195,11120441,10,0.5,''),(593195,11120485,16,24,''),
(633692,1123,1,2,''),(633692,2010,4,1.5,''),(633692,2047,4,0.25,''),(633692,2050,4,1,''),(633692,8120,3,3,'dry'),(633692,9040,1,2,'ripe, mashed'),(633692,9079,3,1,''),(633692,18371,4,2,''),(633692,93607,3,1,'unsweetened'),(633692,10012220,5,1,''),
(633754,1019,9,4,'diced'),(633754,2044,3,0.5,'fresh chopped'),(633754,11209,8,1,'diced unpeeled'),(633754,11215,1,5,'chopped'),(633754,11291,1,1,''),(633754,11333,1,2,'diced green'),(633754,11477,8,1,''),(633754,1022068,5,2,'red'),(633754,10011693,8,2,'chopped'),
(640693,2003,10,1,'dried'),(640693,2027,10,1,'dried'),(640693,11209,3,1,'diced'),(640693,11215,33,2,'minced'),(640693,11291,3,0.25,'diced'),(640693,11477,3,1,'diced'),(640693,11821,3,1,'diced red'),(640693,14412,3,0.25,''),(640693,1082047,21,1,''),(640693,10012222,10,0.5,'black'),(640693,11120421,16,2,'cooked'),(640693,11120517,31,1,''),(640693,11120518,3,1,'peeled'),
(658523,1036,3,1,''),(658523,1123,1,1,''),(658523,1159,9,8,''),(658523,10123,27,6,'uncooked'),(658523,11485,14,1.5,'smashed (see note)'),(658523,99226,4,2,'dried fresh minced for garnish'),(658523,1001026,14,2,'shredded'),(658523,1002030,10,0.125,'black'),(658523,1082047,10,0.25,''),(658523,10111549,14,3,'( 25 oz or so)'),(658523,11120487,1,9,''),
(659109,2047,11,1,'Salt'),(659109,4053,5,3,'Olive Oil (divided)'),(659109,6615,9,32,'Organic Vegetable Stock'),(659109,11215,1,5,'Garlic Cloves (finely chopped)'),(659109,11233,12,8,'Kale (stems removed and cut into ribbons)'),(659109,11282,23,1,'Onion (diced)'),(659109,15076,9,10,'Poached Salmon (flaked)'),(659109,20035,3,1,'Quinoa'),(659109,10211821,11,1,'Pepper'),
(715769,4053,5,1,NULL),(715769,6615,14,2,NULL),(715769,11215,1,1,'minced'),(715769,11282,3,0.5,NULL),(715769,12155,15,2,'chopped'),(715769,20035,3,1,'rinsed'),(715769,98840,13,1,'trimmed'),
(716444,1123,1,1,''),(716444,2047,4,1,''),(716444,10012221,5,1,''),(716444,10012224,4,1,'ground'),
(716446,1123,1,5,''),
(716627,2009,4,2,''),(716627,6168,19,4,''),(716627,11291,3,0.25,'chopped'),(716627,14412,5,3,''),(716627,16018,18,15,'drained canned'),(716627,1002014,4,0.5,''),(716627,1002030,4,0.25,'black'),(716627,10011693,18,10,'diced with green chilies, not drained canned'),(716627,11120521,3,0.5,'uncooked'),
(719503,9037,1,0.5,'peeled mashed sliced'),(719503,10011693,20,1,''),(719503,11120474,11,1,'flaked'),(719503,11120545,12,1,'for garnish'),(719503,11120546,4,1,''),(719503,11120547,3,0.333333,'low-fat'),(719503,11120549,39,1,'whole-wheat hearty'),
(798400,14412,5,5,'as needed'),(798400,10011693,23,1,'finely chopped'),(798400,11120454,10,0.5,''),(798400,11120460,10,0.5,''),(798400,11120467,14,2,'dried cooked'),(798400,11120468,4,2,''),(798400,11120469,10,0.5,''),(798400,11120470,8,1,'italian'),(798400,11120471,10,0.5,''),(798400,11120472,1,3,''),(798400,11120473,1,2,'green red seeded finely chopped'),(798400,11120474,10,1,'to taste'),(798400,11120475,1,2,''),(798400,11120476,13,1,'trimmed chopped');
/*!40000 ALTER TABLE `recipe_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe_instructions`
--

DROP TABLE IF EXISTS `recipe_instructions`;
CREATE TABLE `recipe_instructions` (
  `recipeID` int(11) NOT NULL,
  `stepNumber` tinyint(4) NOT NULL,
  `stepInfo` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`recipeID`,`stepNumber`),
  CONSTRAINT `fk_recipe_instructions` FOREIGN KEY (`recipeID`) REFERENCES `recipe` (`recipeID`) ON DELETE CASCADE
);

--
-- Dumping data for table `recipe_instructions`
--

LOCK TABLES `recipe_instructions` WRITE;
/*!40000 ALTER TABLE `recipe_instructions` DISABLE KEYS */;
INSERT INTO `recipe_instructions` VALUES (61761,1,'In a medium bowl, stir together the Oreo cookie crumbs and melted butter.'),(61761,2,'Evenly divide the crumbs between your individual serving dishes and press into the bottoms of the dishes to form a crust layer.'),(61761,3,'In a large bowl, with an electric mixer, beat the cream cheese and Nutella until smooth.'),(61761,4,'Add vanilla and mix to combine.'),(61761,5,'Using a rubber spatula, fold in the whipped topping until well blended and no streaks remain.'),(61761,6,'Evenly pipe or spoon the filling into individual serving dishes.'),(61761,7,'Cover with plastic wrap and refrigerate for at least 2 hours before serving.'),(61761,8,'If desired, garnish with additional whipped topping, chocolate shavings, and/or toasted, chopped hazelnuts.'),
(84206,1,'Spread about 2/3 cup Praline Cream evenly between each cake layer and about 1 cup on top of cake.'),(84206,2,'Spread a thin coat of Praline Cream (about 1 cup) on sides of cake.'),(84206,3,'Press pecans into sides of cake.'),(84206,4,'Garnish, if desired.'),(84206,5,'Cover with plastic wrap; chill until ready to serve, or freeze, if desired.'),
(222545,1,'Grind the garlic, ginger, orange zest and spring onions to a paste in a food processor.'),(222545,2,'Add the honey, orange juice, soy sauce and oil, then blend again.'),(222545,3,'Pour the mixture over the cubed chicken and leave to marinate for at least 1 hr, but preferably overnight.'),(222545,4,'Toss in the mushrooms for the last half an hour so they take on some of the flavour, too.'),(222545,5,'Thread the chicken, tomatoes, mushrooms and peppers onto 20 wooden skewers, then cook on a griddle pan for 7-8 mins each side or until the chicken is thoroughly cooked and golden brown.'),(222545,6,'Turn the kebabs frequently and baste with the marinade from time to time until evenly cooked.'),(222545,7,'Arrange on a platter, scatter with chopped spring onion and eat with your fingers.'),
(224732,1,'Heat oven to 190C/fan 170C/gas'),(224732,2,'Work the flour, butter and sugar together with your hands until well mixed, then press onto the base and up the sides of six 10cm fluted tart tins.'),(224732,3,'Put on a baking tray.'),(224732,4,'Reserve 36 pecan halves and roughly chop the rest.'),(224732,5,'Beat together the eggs, sugar, syrup, vanilla, bourbon, melted butter and chopped pecans, and spoon into the tart cases.'),(224732,6,'Top each one with 6 pecan halves, then bake for 20-25 mins until golden and set.'),(224732,7,'The filling will rise up as it bakes, but will settle back as it cools.'),(224732,8,'You can make these 2 days ahead and freeze for up to 2 months.'),(224732,9,'If you fancy a double shot of bourbon, serve the pies warm topped with this iced bourbon cream.'),(224732,10,'Beat the whipping cream with the golden syrup and bourbon until softly stiff, then turn into a rigid container and freeze for up to 1 month.'),(224732,11,'Serve straight from the container like ice-cream, or leave to soften for up to 15 mins for a softer, creamy version.'),
(245775,1,'Soak the skewers in water for at least a half an hour before grilling.'),(245775,2,'Marinate the shrimp: In a large bowl whisk together the spices—the paprikas, cumin, garlic, salt, and pepper—and the lime juice and olive oil.'),(245775,3,'Add the shrimp and toss to coat with the marinade.'),(245775,4,'Keep chilled for half an hour to an hour.'),(245775,5,'Grill the shrimp: Prepare grill for medium-high direct heat, or heat a grill pan.'),(245775,6,'Thread the shrimp onto skewers (it helps to double thread with two skewers at a time to make it easier to turn over on the grill).'),(245775,7,'Baste the grill grates with some olive oil so that the shrimp don\'t stick to the grill.'),(245775,8,'Grill or cook the shrimp a few minutes per side (2-4, depending on the size of the shrimp), until the shrimp are just cooked through.'),(245775,9,'Remove from grill and serve immediately.'),
(245916,1,'Cut all the fish and veggies into similar-sized pieces; this helps everything lay flat when it is on the grill.'),(245916,2,'Marinate the fish and vegetables: To make the marinade, purée the onion, rosemary, garlic, salt and pepper in a food processor.'),(245916,3,'Drizzle in the olive oil while puréeing, continue to purée until smooth, about 1-2 minutes.'),(245916,4,'Coat the fish and veggies in the marinade.'),(245916,5,'Set in the fridge for at least an hour and up to overnight.'),(245916,6,'Thread onto skewers: When skewering the fish and vegetables, pierce the fish against the grain, and select pieces of veggies that are close to the same size as your fish.'),(245916,7,'This is important, because if the pieces are different widths, some things will be charred and others undercooked.'),(245916,8,'You also want to be careful when loading up the skewers; it\'s easy to stab yourself by accident!'),(245916,9,'Alternate pieces of fish with pieces of various veggies, leaving a little space between everything.'),(245916,10,'Don’t crowd the skewer, or the parts that are touching will cook too slowly.'),(245916,11,'Note that by threading the skewers with assorted veggies and fish, some things will be cooked more or less than others, as some things take longer to cook than others.'),(245916,12,'If you want all of your items to be cooked perfectly, use a separate skewer for the onions, one for the tuna, one for the bell peppers, etc.'),(245916,13,'Put the onions and bell peppers down first because they take longer to cook.'),(245916,14,'Grill on high, direct heat: Prepare the grill for high, direct heat.'),(245916,15,'Clean the grates and wipe them down with a paper towel that has been dipped in vegetable oil.'),(245916,16,'Lay the skewers on the grill.'),(245916,17,'Don\'t move them until the fish pieces are well browned on one side, about 3-6 minutes.'),(245916,18,'Then using tongs, carefully turn the skewers over and cook them until they are seared on the other side.'),(245916,19,'Serve hot or at room temperature.'),(245916,20,'Drizzle with lemon juice or serve with lemon wedges.'),
(246995,1,'Cut the chicken into 1 1/2 inch pieces and place in a bowl.'),(246995,2,'Mix with the wine, oil, rosemary, and pepper.'),(246995,3,'Cover and set aside to marinate in the refrigerator for one hour.'),
(247337,1,'Make the teriyaki marinade: In a medium bowl, mix together the soy sauce, mirin or rice vinegar, and brown sugar, until the sugar is completely dissolved.'),(247337,2,'Add the fresh ginger, minced green onions, chili pepper flakes, and vegetable oil.'),
(247362,1,'Mix cheese, herbs, lemon zest, pepper: Preheat oven to 350°F.'),(247362,2,'Mix first goat cheese, herbs, lemon zest, and black pepper in a small bowl to blend.'),(247362,3,'Set aside.'),(247362,4,'(The cheese mixture can be mixed a couple of days ahead of time.'),(247362,5,'Toast bread slices in oven: '),(247362,6,'Brush oil over both sides of bread slices.'),(247362,7,'Arrange bread in single layer on a large baking sheet.'),(247362,8,'Bake until bread is just crisp, about 5 minutes on each side.'),(247362,9,'(You can prepare the toasts a couple hours in advance.'),
(263078,1,'Combine soymilk, banana, cinnamon, a light dash of nutmeg (about 1/8 tsp) and a tiny little bit of cloves in a blender or vita-mix and blend until smooth and creamy.'),(263078,2,'Garnish with a dash of nutmeg if desired.Nutritional Information'),
(509198,1,'Lightly coat a frying pan with oil and scramble the three eggs, seasoning them with salt and pepper.'),(509198,2,'Just before they look done, top the eggs with the feta cheese and remove the skillet from the heat.'),(509198,3,'(Eggs will continue to cook in the hot skillet).'),(509198,4,'Warm two flour tortillas in the oven on low or in the microwave for 20 seconds.'),(509198,5,'Divide eggs in half and place into tortillas.'),(509198,6,'Top with tomatoes and avocado and sprinkle with a bit more sea salt and pepper.'),
(593195,1,'In a small bowl, gently mix together watermelon, avocado, feta, and lime juice.'),(593195,2,'Place small spoonfuls of topping onto each bruschetta cracker.'),(593195,3,'Serve immediately.'),
(633692,1,'Preheat oven to 350F'),(633692,2,'Mix dry ingredients until well combined'),(633692,3,'Mix liquid ingredients separately'),(633692,4,'Add the liquid ingredients to the dry mix. Mix well'),(633692,5,'Spray a pan with a non-stick spray - the smaller the pan, the thicker the bars will be'),(633692,6,'Pour the mix into the pan'),(633692,7,'Bake for 45 minutes'),
(633754,1,'Heat oil in a heavy, large Dutch oven over medium heat.'),(633754,2,'Add garlic; stir 1 minute.'),(633754,3,'Add eggplant, green bell peppers, tomatoes, onion, zucchini and basil.'),(633754,4,'Saute for 5 minutes.'),(633754,5,'Cover and simmer until all vegetables are tender, stirring occasionally, about 25 minutes.'),(633754,6,'Uncover pot and simmer until juice thickens, stirring occasionally, about 10 minutes.'),(633754,7,'Mix in vinegar; season to taste with salt and pepper.'),(633754,8,'Preheat oven to 350 degrees.'),(633754,9,'Spread in 9-inch pie dish.'),(633754,10,'Sprinkle with cheese, if desired.'),(633754,11,'Bake until heated through, about 20 minutes.'),(633754,12,'This recipe yields about 3 cups.'),
(640693,1,'Heat olive oil in a large skillet over medium heat.'),(640693,2,'Add onion, bell pepper, and zucchini and saut for about 7 minutes, stirring occasionally, until slightly softened.'),(640693,3,'Add eggplant and continue to saut mixture for another 3 minutes, stirring frequently.'),(640693,4,'When vegetables are softened, add garlic and cook until fragrant, about 1 minute.'),(640693,5,'Add dried spices, salt, and pepper, followed by the tomato sauce and water.'),(640693,6,'Allow mixture to come to a simmer and cook for 5 minutes, until slightly thickened.'),(640693,7,'Stir in mascarpone cheese and serve immediately.'),
(658523,1,'Preheat oven to 375 degrees F.'),(658523,2,'Fry the bacon (sliced or whole, whichever you prefer) in a medium saucepan over medium heat until it crisps up.'),(658523,3,'Remove bacon with a slotted spoon and set aside.'),(658523,4,'Meanwhile, remove leaves from the 2 sprigs of fresh sage and add to the hot bacon drippings after you remove the cooked bacon.'),(658523,5,'Fry for about 45 seconds or so until they, too, crisp up.'),(658523,6,'Remove fried leaves from skillet and set aside with cooked bacon bits.'),(658523,7,'In a small bowl combine the goat cheese, ricotta cheese, egg, 2 T minced fresh sage, salt & pepper.'),(658523,8,'Mix thoroughly.'),(658523,9,'Set aside.'),(658523,10,'Prepare an 11x7 inch casserole dish with nonstick spray and add spread a couple spoonfuls of marinara sauce in the bottom.'),(658523,11,'Put a layer of lasagna noodles over the sauce.'),(658523,12,'I used three, but I had to break one to make it fit.'),(658523,13,'Spread about 1/2 cup smashed squash over noodles, followed by 1/3 of the goat cheese mixture, 1/3 of the marinara sauce and 1/2 cup shredded mozzarella.'),(658523,14,'Repeat layering two more times with remaining ingredients and top with remaining mozzarella cheese.'),(658523,15,'Cover with aluminum foil and bake for 30 minutes.'),(658523,16,'Remove foil and continue to bake, uncovered, for another 15 minutes until everything is bubbly, heated through, and the cheese begins to turn golden brown.'),(658523,17,'Remove from the oven and top with reserved bacon bits and fried sage.'),(658523,18,'Allow to rest for about 10 minutes before slicing.'),
(659109,1,'In a 4 quart saucepan, heat 2 tablespoons of olive oil over medium high heat.'),(659109,2,'When oil is shimmering, add diced onion.'),(659109,3,'Saute onion until transparent.'),(659109,4,'Add quinoa to onion mixture and stir, to toast quinoa, for 2 minutes.'),(659109,5,'Add 1 cup of vegetable stock to quinoa and onions.'),(659109,6,'Stir until stock is absorbed.  Once stock is absorbed, add 1 cup of stock.'),(659109,7,'Continue stirring until stock is absorbed.'),(659109,8,'Add remaining stock in 1/2 cup intervals, stirring until all stock is absorbed.'),(659109,9,'Remove from heat.'),(659109,10,'While preparing the onion quinoa mixture, heat 1 tablespoon of oil in a saute pan with chopped garlic (over medium high heat).'),(659109,11,'Once garlic is sizzling, add chopped kale to the pan.'),(659109,12,'Turn kale to coat with oil and garlic.'),(659109,13,'Turn kale mixture until fragrant (approximately 2 minutes).'),(659109,14,'Remove kale mixture from heat.'),(659109,15,'Once quinoa is complete, add kale and salmon.'),(659109,16,'Stir to combine.'),(659109,17,'Add salt and pepper to taste.'),
(715769,1,'In a large pan with lid heat olive oil over medium high heat.'),(715769,2,'Add onions and cook for 1 minute.'),(715769,3,'Add garlic and cook until onions are translucent and garlic is fragrant.'),(715769,4,'Add quinoa to pan, stir to combine. Slowly add in broth and bring to a boil. Cover and reduce heat to low, cook for 15 minutes.'),(715769,5,'In the last 2-3 minutes of cooking add in broccolini on top of the quinoa (do not stir) and cover.'),(715769,6,'Uncover and toss broccolini and quinoa together. Season to taste with salt and pepper.'),(715769,7,'Add walnuts and serve hot.'),
(716444,1,'add butter to pre-heated pan, melt and spread evenly'),(716444,2,'break egg into pan'),(716444,3,'add salt and pepper to your liking and scramble'),(716444,4,'cook until done'),
(716446,1,'boil water in deep pot'),(716446,2,'when boiling, cook eggs in water for 12 minutes'),(716446,3,'pour boiling water out of the pot and let eggs cool off'),
(716627,1,'Heat the olive oil in a large pot over medium heat.'),(716627,2,'Add onions and saute until soft, or for about 5 minutes.'),(716627,3,'Add all other remaining ingredients and stir together.'),(716627,4,'Increase the heat to medium high and bring to a boil.'),(716627,5,'Cover and reduce heat to medium low so that the mixture simmers.'),(716627,6,'Cook for 15-20 minutes, or until rice is fluffy and liquid is absorbed.'),(716627,7,'Serve with salsa, cheese, and sour cream.'),
(719503,1,'Toast the bread and drizzle with flaxseed oil.'),(719503,2,'Layer with the avocado, cottage cheese and tomato.'),(719503,3,'Garnish with basil leaves and flaked sea salt and drizzle with a little more flaxseed oil if desired.'),
(798400,1,'Rinse the black-eyed peas and soak in several inches of water for 6 hours or overnight.'),(798400,2,'Drain and rinse, then transfer to a large saucepan and cover with fresh water.'),(798400,3,'Bring to a boil, reduce heat to medium-low, cover, and simmer for 40 to 60 minutes.'),(798400,4,'Take care not to overcook  the beans should be tender but not be falling apart.'),(798400,5,'Drain and set aside.'),(798400,6,'To prepare the eggplant, cut of the stem and bottom edge and then cut in half lengthwise.'),(798400,7,'Score the flesh into diagonal 1-inch lines, then turn and score again until you have a diagonal pattern.'),(798400,8,'Take care not to cut through the skin.'),(798400,9,'Sprinkle with some salt and let sit for 40 minutes.'),(798400,10,'Rinse and squeeze out any excess water.'),(798400,11,'Brush the eggplant with some oil and transfer to a roasting pan.'),(798400,12,'Bake in a preheated 400 oven until the flesh appears collapsed and is wrinkly.'),(798400,13,'Remove from heat and let cool for about 10 minutes, season with a bit of salt, and remove the flesh from the eggplant.'),(798400,14,'If there is too much water, drain in a strainer.'),(798400,15,'Set aside.'),(798400,16,'Heat the oil over medium heat in the same saucepan used to cook the black-eyed peas.'),(798400,17,'When hot, toss in the shallots and chilies and saut for 2 to 3 minutes.'),(798400,18,'Now add the spices and stir for another minute, until fragrant.'),(798400,19,'Add the tomato, cook for another few minutes, and then add the eggplant and black-eyed peas, and cook for another few minutes, stirring often.'),(798400,20,'Pour a few tablespoons of water into the pan and add handfuls of chard at a time until wilted.'),(798400,21,'Add more water as necessary.'),(798400,22,'Add the lemon juice and salt to taste near the end of the cooking time.'),(798400,23,'Remove from heat, cover, and let sit for a few minutes before serving.');
/*!40000 ALTER TABLE `recipe_instructions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `standard_measurement`
--

DROP TABLE IF EXISTS `standard_measurement`;
CREATE TABLE `standard_measurement` (
  `measurementID` int(11) NOT NULL AUTO_INCREMENT,
  `measurementName` varchar(20) NOT NULL,
  PRIMARY KEY (`measurementID`),
  UNIQUE KEY `measurementName` (`measurementName`)
);

--
-- Dumping data for table `standard_measurement`
--

LOCK TABLES `standard_measurement` WRITE;
/*!40000 ALTER TABLE `standard_measurement` DISABLE KEYS */;
INSERT INTO `standard_measurement` VALUES 
(41,'6-inch'),(40,'8-inch'),(13,'bunch'),(37,'clove'),(33,'cloves'),(3,'cup'),(14,'cups'),(19,'dashes'),(35,'g'),(38,'Halves'),(22,'inch'),(26,'jars'),
(32,'knob'),(8,'large'),(34,'lb'),(29,'lbs'),(12,'leaves'),(23,'medium'),(2,'ml'),(18,'ounce'),(9,'ounces'),(15,'oz'),(25,'package'),(21,'pinch'),(24,'pound'),
(28,'pounds'),(11,'serving'),(16,'servings'),(17,'sheets'),(39,'slice'),(27,'slices'),(20,'small'),(36,'small pinch'),(31,'tablespoon'),(5,'tablespoons'),(30,'tbs'),
(10,'teaspoon'),(4,'teaspoons'),(1,'unit');
/*!40000 ALTER TABLE `standard_measurement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorites`
--

DROP TABLE IF EXISTS `user_favorites`;

CREATE TABLE `user_favorites` (
  `userID` int(11) NOT NULL,
  `recipeID` int(11) NOT NULL,
  PRIMARY KEY (`userID`,`recipeID`),
  KEY `fk_user_favorites_recipe` (`recipeID`),
  CONSTRAINT `fk_user_favorites_recipe` FOREIGN KEY (`recipeID`) REFERENCES `recipe` (`recipeID`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_favorites_user` FOREIGN KEY (`userID`) REFERENCES `accounttable` (`userID`) ON DELETE CASCADE
);

