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
      <fo:region-body margin-top="15mm" margin-bottom="15mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="20mm"/>
      <fo:region-after extent="10mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>



  <fo:page-sequence master-reference="page">

    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">

      <fo:block text-align="right" font-family="Times" font-size="8pt" font-weight="bold">ANEXA NR. ${doc["child::response/child::record/child::field[attribute::name='anexa']"]}</fo:block>

      <fo:block font-weight="bold" text-align="center" font-family="Times" font-size="10pt">
	La Contractul Nr. ${doc["child::response/child::record/child::field[attribute::name='contract']"]}
      </fo:block>
      <fo:block font-weight="bold" text-align="center" border-after-style="solid" font-family="Times" font-size="8pt">
	Beneficiar: ${doc["child::response/child::record/child::field[attribute::name='clientName']"]}
      </fo:block>
    </fo:static-content>
    <!--/HEADER-->


    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="8pt" border-after-style="solid">
	Anexa ${doc["child::response/child::record/child::field[attribute::name='anexa']"]}
	La Contract Nr. ${doc["child::response/child::record/child::field[attribute::name='contract']"]}
	Pagina <fo:page-number/>
	din <fo:page-number-citation ref-id="last-page-block"/>
      </fo:block>
      <fo:block text-align="center" font-size="8pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">


      <fo:block font-weight="bold" space-after="5mm" font-family="Times" font-size="10pt">
	Obiectiv: &#x00AB;${doc["child::response/child::record/child::field[attribute::name='description']"]?html} &#x00BB;
      </fo:block>

      <!-- liniile ofertei -->
      <fo:block font-size="10pt" font-family="Times">

      <#assign lineno=1>
      <#assign lineoffers=group_line_offers(doc) >
      <!-- lineoffers.usilines are ${lineoffers.usilines?size} elemente -->
      <!-- lineoffers.usi are ${lineoffers.usi?size} elemente -->
      <#assign gcodes=lineoffers.usi?keys>
      <#assign offer_value = 0>
      <#list gcodes as code>
        <!-- usile cu gcode: ${code} -->
	<!-- offer_value este ${offer_value} -->
        <@display_usi lineno=lineno usi=lineoffers.usi[code]/>
	<!-- offer_value este ${offer_value} -->
	<#assign lineno = lineno+1 >
      </#list>

      <!-- offer_value este ${offer_value} -->
      <@display_sisteme lineno=lineno sisteme=lineoffers.sisteme/>
      <!-- offer_value este ${offer_value} -->


      </fo:block>
      <!-- /liniile ofertei -->
 

      
      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-decoration="underline" text-align="right" space-before="1cm">TOTAL CONTRACT NR  ${doc["response/record/field[attribute::name='contract']"]}</fo:block>
      
      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-align="right">TOTAL fara TVA: ${((offer_value!0)/(cursul?number))?string("#,##0.00")} ${moneda}</fo:block>
      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-align="right">TOTAL cu TVA: ${((offer_value!0)/(cursul?number) * 1.24)?string("#,##0.00")} ${moneda}</fo:block>


      <fo:table width="100%" font-family="Times" font-size="10pt" font-weight="bold" space-before="1cm">
	<fo:table-column column-number="1" width="50%"/>
	<fo:table-column column-number="2" width="50%"/>

	<fo:table-body>
	  <fo:table-row>
	    <fo:table-cell column-number="1">
	      <fo:block text-align="left">Vanzator</fo:block>
	    </fo:table-cell>
	    <fo:table-cell column-number="2">
	      <fo:block text-align="right">Cumparator</fo:block>
	    </fo:table-cell>
	  </fo:table-row>
	  <fo:table-row>
	    <fo:table-cell column-number="1">
	      <fo:block space-before="4mm">Director General</fo:block>
	      <fo:block>Enache Constantin</fo:block>
	      <fo:block space-before="4mm">Contabil Sef</fo:block>
	      <fo:block>Marculescu Iustina</fo:block>
	    </fo:table-cell>
	    <fo:table-cell column-number="2">
	      <#list doc["response/record/field[attribute::name='contacts']/record"] as contact>
	      <#if contact["field[attribute::name='department']"] = "Aprobari">
	      <fo:block space-before="4mm" text-align="right">${contact["field[attribute::name='title']"]}</fo:block>
	      <fo:block text-align="right">
		${contact["field[attribute::name='firstName']"]}
		${contact["field[attribute::name='lastName']"]}
	      </fo:block>
	      </#if>
	      </#list>
	      <fo:block/><!-- ma asigur ca am cel putin un fo:block in celula de tabel -->
	    </fo:table-cell>
	  </fo:table-row>
	</fo:table-body>
      </fo:table>

      <fo:block text-align="center" font-family="Times" font-size="10pt" font-weight="bold" space-before="1cm">COMP. CONTRACTARI</fo:block>

      <!-- Empty block, for page number citation -->
      <fo:block id="last-page-block"></fo:block>
    </fo:flow>
    <!-- /BODY -->
  </fo:page-sequence>
</fo:root>
