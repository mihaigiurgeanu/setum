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
      <fo:region-body margin-top="30mm" margin-bottom="15mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="30mm"/>
      <fo:region-after extent="10mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>



  <fo:page-sequence master-reference="page">

    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block border-before-style="solid"
		border-after-style="solid">
	<fo:table border-collapse="collapse">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body border-collapse="collapse">
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>
		  <fo:external-graphic src="images/sigla.png" content-height="20mm"/>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell font-size="8pt" display-align="center">
		<fo:block text-align="center">BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6</fo:block>
		<fo:block text-align="center">Tel.:  021.316.39.57, 021.316.05.78, 021.316.18.56 Tel/Fax: 021.316.05.90</fo:block>
		<fo:block text-align="center">e-mail: conducere@setumsa.ro; desfacere@setumsa.ro; contractari@setumsa.ro</fo:block>
                <fo:block text-align="center">www.setumsa.ro</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>



    </fo:static-content>
    <!--/HEADER-->


    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="8pt"
		border-after-style="solid">
	Oferta ${doc["child::response/child::record/child::field[attribute::name='no']"]} 
	din ${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]}
	Pagina <fo:page-number/>
	din <fo:page-number-citation ref-id="last-page-block"/>
      </fo:block>
      <fo:block text-align="center" font-size="8pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">


      <!-- date generale despre oferta (numar, data, nume client, etc.) -->
      <fo:block font-size="10pt" font-family="Times">
	<fo:table width="16.1cm" border-collapse="collapse">
	  <fo:table-column column-number="1" column-width="25%"/>
	  <fo:table-column column-number="2" column-width="25%"/>
          <fo:table-column column-number="3" column-width="50%"/>


	  <fo:table-body>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="10pt" number-columns-spanned="2">
                 <fo:block>Catre: ${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="10pt">
                 <fo:block>De la: SC SETUM SA</fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="10pt" number-columns-spanned="2">
                 <fo:block>In atentia: ${doc["child::response/child::record/child::field[attribute::name='clientContactName']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="10pt">
                 <fo:block>Nr oferta: ${doc["child::response/child::record/child::field[attribute::name='no']"]}/Data: ${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]}</fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="10pt" number-columns-spanned="1">
                 <fo:block>Tel: ${doc["response/record/field[attribute::name='clientContactPhone']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="2" border-style="solid" font-size="10pt" number-columns-spanned="1">
                 <fo:block>Fax: ${doc["response/record/field[attribute::name='clientContactFax']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="10pt">
                 <fo:block>Nr. pagini: <fo:page-number-citation ref-id="last-page-block"/></fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="10pt" number-columns-spanned="2">
                 <fo:block>E-mail: ${doc["response/record/field[attribute::name='clientContactEmail']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="10pt">
                 <fo:block>Referitor la cererea Dvs. de oferta nr: ${doc["response/record/field[attribute::name='name']"]}</fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="10pt" number-columns-spanned="3">
                 <fo:block>Prin prezenta va facem cunoscuta oferta noastra de pret pentru:</fo:block>
                 <fo:block>Obiectiv:</fo:block>
                 <fo:block>${doc["child::response/child::record/child::field[attribute::name='description']"]?html}</fo:block>
              </fo:table-cell>
            </fo:table-row>
	  </fo:table-body>
	</fo:table>
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
 

      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-decoration="underline" text-align="right" space-before="1cm">TOTAL OFERTA NR  ${doc["response/record/field[attribute::name='no']"]}/Data ${doc["response/record/field[attribute::name='dateFrom']"]}</fo:block>
      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-align="right">TOTAL fara TVA: ${((offer_value!0)/(cursul?number))?string("#,##0.00")} ${moneda}</fo:block>
      <fo:block font-family="Times" font-size="10pt" font-weight="bold" text-align="right">TOTAL cu TVA: ${(((offer_value!0)/(cursul?number)) * 1.19)?string("#,##0.00")} ${moneda}</fo:block>

      <fo:block font-family="Times" font-size="10pt">
	<fo:inline font-weight="bold">OBSERVATII:</fo:inline>
	${doc["response/record/field[attribute::name='comment']"]?html}
      </fo:block>

      <fo:block font-family="Times" font-size="10pt" font-weight="bold">
	<fo:block>Valabilitatea ofertei: ${doc["response/record/field[attribute::name='dateTo']"]}</fo:block>
	<!--
	<fo:block>Termen de livrare:<fo:inline white-space-collapse="false">___</fo:inline>zile lucratoare</fo:block>
	<fo:block>Garantie: 12 luni de la data livarii produsului.</fo:block>
	<fo:block>Termeni contractuali: avans 50% la data semnarii contractului, diferenta de 50% la data livrarii produsului, in lei la cursul BNR din ziua facturarii.</fo:block>
	-->
	<fo:block linefeed-treatment="preserve">
	  ${doc["response/record/field[attribute::name='terms']"]}
	</fo:block>

	<fo:block font-family="Times" font-size="10pt" font-weight="bold" space-before="3mm" text-align="right">
	  <fo:block>Departamentul Contractari</fo:block>
	  <fo:block>${doc["response/record/field[attribute::name='attribute1']"]}</fo:block>
	</fo:block>
      </fo:block>


      <!-- Empty block, for page number citation -->
      <fo:block id="last-page-block"></fo:block>
    </fo:flow>
    <!-- /BODY -->
  </fo:page-sequence>
</fo:root>
