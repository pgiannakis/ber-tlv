 BER-TLV encoder/decoder
=======
This is an implementation of BER-TLV (Basic Encoding Rules - Tag Length Value).

[EMV 4.3](http://www.emvco.com/specifications.aspx?id=223), Book3 - Application Specification,Annex B, p 161 - 164 defines the 'Rules for BER-TLV Data Objects

The project includes a test suite for the critical operations such as length coding and tag field representation

The design of TLV encoder/decoder based on [composite design pattern](http://en.wikipedia.org/wiki/Composite_pattern)
![ber_tlv](https://cloud.githubusercontent.com/assets/7859558/4962381/79aa72d4-66de-11e4-9cf2-8522716e8d76.jpg)
