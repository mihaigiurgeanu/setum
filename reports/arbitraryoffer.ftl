<?xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<!-- <#include "arbitraryoffer-macros.ftl"> -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-height="297mm"
			   page-width="210mm"
			   margin-top="20mm"
			   margin-bottom="10mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="40mm" margin-bottom="15mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="30mm"/>
      <fo:region-after extent="10mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>

  <fo:page-sequence master-reference="page">
    <fo:static-content flow-name="xsl-region-before">

      <!--HEADER-->

      <fo:block border-before-style="solid"
		border-after-style="solid">
	<fo:table table-layout="auto">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body border-collapse="true">
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>
		  <fo:external-graphic src="images/sigla.png" content-height="20mm"/>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell font-size="10pt" display-align="center">
		<fo:block text-align="end">BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6</fo:block>
		<fo:block text-align="end">Contractari: Tel/Fax: 316.05.90 Tel: 316.18.56; 316.05.88</fo:block>
		<fo:block text-align="end">Secretariat: Tel/Fax: 316.05.88; Tel: 316.39.57</fo:block>
		<fo:block text-align="end">Centrala: Tel: 316.05.78 Dep. Economic: Tel: 317.25.40</fo:block>
		<fo:block text-align="end">Magazin OBOR: sos. Mihai Bravu 6, Tel/Fax: 252.49.67</fo:block>
		<fo:block text-align="end">Web: http://www.setumsa.ro; e-mail: desfacere@setum.ro contractari@setum.ro</fo:block>
		<fo:block text-align="end">Capital social: 14,10 miliarde lei</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>


      <!--/HEADER-->

    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="6pt"
		border-before-style="solid">Pagina <fo:page-number/></fo:block>
    </fo:static-content>




    <fo:flow flow-name="xsl-region-body">
      <!-- date generale despre oferta (numar, data, nume client, etc.) -->
      <fo:block>
	<fo:table width="90%">
	  <fo:table-column column-number="1" column-width="30%"/>
	  <fo:table-column column-number="2" column-width="70%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block font-family="Times" font-size="11pt">Oferta Nr:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block font-family="Times" font-size="11pt">${doc["child::response/child::record/child::field[attribute::name='no']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block font-family="Times" font-size="11pt">Catre:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block font-family="Times" font-size="11pt">${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block font-family="Times" font-size="11pt">Din data:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block font-family="Times" font-size="11pt">${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block font-family="Times" font-size="11pt">Valabilitate pana:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block font-family="Times" font-size="11pt">${doc["child::response/child::record/child::field[attribute::name='dateTo']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block font-family="Times" font-size="11pt">Observatii:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block font-family="Times" font-size="11pt">${doc["child::response/child::record/child::field[attribute::name='description']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	  </fo:table-body>
	</fo:table>
      </fo:block>



      <!-- liniile ofertei -->
      <#assign lineno=1>
      <#list doc["child::response/child::record/child::field[attribute::name='lines']/child::record"] as record>
      <fo:block text-align="justified" font-size="11pt" font-family="Times">
	${lineno}. ${record["child::field[attribute::name='productCategory']"]} - ${record["child::field[attribute::name='productCode']"]}: RON ${record["child::field[attribute::name='vatPrice']"]}
      </fo:block>
      <#switch record["child::field[attribute::name='product']/child::record/child::field[attribute::name='category.id']"]?number>
      <#case 9990>
      <#case 9993>
      <fo:block font-size="8pt">
	<#visit record["child::field[attribute::name='product']/child::record"]>
      </fo:block>
      </#switch>

      <#assign lineno = lineno + 1>
      </#list>




    </fo:flow>


  </fo:page-sequence>
</fo:root>
