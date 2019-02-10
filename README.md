# MagicNumbers
EPAM Java Academy

Application provides validation of file extension.
User have to put full path to file in application in order to check its type. 
It is possible to modify interface of FileExtensionHandler’s in future to add/load new rules (of creating files signatures) from console and/or from database.
ExtensionRulesConteiner gathers all necessary information such as which bits of bytes should be set to consider file type as expected. 
The class stores indexes of bytes and values which should be represented by each of them for certain file extension. 
Indexes greater then or equal zero indicates from the beginning of the file and the negative ones are responsible for reading bytes from the end of the signature. 
