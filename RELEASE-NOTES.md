Version 7.1.0 changes
---------------------
07/02/2017

1. Added support to pass an IPV6 address with the IPAD field. IMPORTANT NOTE: IPV6 
    addresses are converted to a static IPV4 address within RIS (10.0.0.1).

Version 7.0.0 changes
---------------------
09/12/2017

1. Introducing configuration key - this breaks backward compatibility with older releases
2. Removed the SALT phrase
3. Maven improvements and code updates

Version 6.5.2 changes
---------------------
08/08/2017

1. Removed request parameters CCMM and CCYY

Version 6.5.1 changes
---------------------
06/23/2017

1. Improved communication logging
2. Added more information to connection headers
3. Integrated more payment types, see https://api.test.kount.net/rpc/support/payments.html

Version 6.5.0 changes
---------------------
05/29/2017

1. SALT phrase configurable as system variable

Version 6.4.2 changes
---------------------
04/06/2017

1. Minor improvements for integration tests logging
2. Fixed build issue when JDK 1.7 is used

Version 6.4.0 changes
---------------------
03/30/2017

1. New requirement for JDK / JRE -- version 1.7 and above 
2. Secure communication between client and server now using TLS v1.2
3. Added Apache Maven tools for easier compilation, build, unit and integration
    tests, javadoc generation, and packaging
4. General source code improvements and modernization
5. General javadoc enhancements

Version 6.3.1 changes
---------------------
05/06/2015

1. Security Patching

Version 6.3.0 changes
---------------------
01/20/2015

1. Added support for additional payment token information (expiration month [MM]
    and expiration year [YYYY]).  The new field names are: CCMM and CCYY.
2. Added support for API keys.  You can now use an API key instead of a
    certificate.  API keys are typically much easier to integrate and maintain.


Version 6.0.0 changes
---------------------
08/01/2014

1. Added support for new 'Kount Central' RIS query modes 'J' and 'W'.


Version 5.5.5 changes
---------------------
09/12/2013

1. Updated sdk_guide.pdf regarding .NET help documentation.

08/27/2013

1. Added new getter functions for the following RIS response fields:
    PIP_IPAD, PIP_LAT, PIP_LON, PIP_COUNTRY, PIP_REGION, PIP_CITY, PIP_ORG,
    IP_IPAD, IP_LAT, IP_LON, IP_COUNTRY, IP_REGION, IP_CITY, IP_ORG, DDFS,
    UAS, DSR, OS, BROWSER

Version 5.5.0 changes
---------------------
06/13/2013

1. Expanded Payment types accepted. Legacy payment setter functions will work,
    but the new generic payment function is recommended. See doc for usage.
2. New getter function for RIS response field, MASTERCARD added.

Version 5.0.0 changes
---------------------
09/27/2012

1. SDK updated to support creating Payment objects using pre-hashed payment
    tokens.

03/08/2012

1. SDK language identifier has been added to the inbound RIS request.
2. All payment tokens are now hashed by default before submitting to RIS. Hence,
    no plain text credit card numbers, Paypal payment IDs, Check numbers, Google
    Checkout IDs, Bill Me Later IDs, Green Dot MoneyPak IDs or gift card numbers
    are transmitted in plain text to RIS by default. The value of the new RIS
    input field LAST4 is automatically set by the SDK for all payment types
    prior to hashing the payment token.
3. Data validation for the RIS request elements "ORDR" and "AUTH" have been
    updated to allow up to 32 characters and null values respectively, matching
    the RIS specification guide.
4. The method com.kount.ris.Response.getReason() has been deprecated and
    replaced with com.kount.ris.Response.getReasonCode(). This new method
    allows the merchant defined decision reason code to be fetched from the
    response.

Version 4.6.0 changes
---------------------
09/19/2011

1. Expanded payment type support. Supported payment types include: Bill Me
    Later, check, credit card, gift card, Green Dot MoneyPak, Google, no
    payment, and PayPal.
2. Added KHASH payment encoding support. Contact your Kount representative for
    more information on this feature.
3. Added method com.kount.ris.Response.getLexisNexisInstantIdAttributes() : Map.
    This is used to fetch LexisNexis Instant ID data that may be associated
    with a RIS transaction. Please contact your Kount representative to have
    Instant ID enabled for your merchant account if you need to access this
    data.

Version 4.5.0 changes
---------------------
06/28/2011

1. Added methods com.kount.ris.getCountersTriggered() and
    com.kount.ris.getNumberCountersTriggered() to get the RIS rules
    counter data associated with a particular transaction.

Version 4.3.5 changes
---------------------
04/04/2011

1. New overloaded method com.kount.ris.setCurrency (String currency) : Inquiry.
    This allows a larger set of currencies to be used where the currency value
    passed to the method is an acceptable code from the ISO 4217 list. The
    method com.kount.ris.setCurrency (CurrencyType currency) : Inquiry has been
    deprecated.

12/14/2010

1. Added method com.kount.ris.Response.getLexisNexisCbdAttributes() : Map. This
    is used to fetch LexisNexis Chargeback Defender data that may be associated
    with a RIS transaction. Please contact your Kount representative to have
    Chargeback Defender enabled for your merchant account if you need to access
    this data.

Version 4.3.0 changes
---------------------
08/18/2010

1. RIS response version 4.3.0 now includes rule IDs and descriptions for all
    rules triggered by the RIS input (request) data based on the RIS rules the
    merchant has defined. The following methods have been added to accommodate
    this change:
    a. com.kount.ris.Response.getRulesTriggered() : Map - Get a Map of the rules
        triggered by this Response.
    b. com.kount.ris.Response.getNumberRulesTriggered() : int - Get the number
        of rules triggered with the response.

Version 4.2.0 changes
---------------------
06/25/2010

This is a quick summary of changes made to the SDK compared to version 4.1.0:

1. All deprecated methods have been removed.
2. Logging has been added. The default logger is a NOP logger that silently
    discards all logging. SIMPLE logger can be turned on which appends logging
    to a selected file. See README for more details.
3. Input data validation has been changed to return all errors encountered in
    the input instead of one error at a time.
4. RIS version 4.2.0 will return warnings associated with bad optional input
    data. RIS 4.2.0 will also return all errors and warnings encountered instead
    of just the first error. Hence, the following accessor methods have been
    added to the class com.kount.ris.Response:
    a. com.kount.ris.Response.getErrors() : Map - Get the errors returned by the
        response.
    b. com.kount.ris.Response.getWarnings() : Map - Get the warnings returned by
        the response.
    c. com.kount.ris.Response.getWarningCount() : int - Get the number of
        warnings contained in the response.
    d. com.kount.ris.Response.getErrorCount() : int - Get the number of errors
        contained in the response.
    e. com.kount.ris.Response.hasErrors() : boolean - Returns true if the
        response has errors.
    f. com.kount.ris.Response.hasWarnings() : boolean - Returns true if the
        response has warnings.
5. The code architecture of the Java SDK has changed significantly from previous
   versions such that this version is no longer binary compatible. Some refactor
   to any existing client code may be required in order to use this version. See
   README file and the Kount RIS SDK guide for code samples.

