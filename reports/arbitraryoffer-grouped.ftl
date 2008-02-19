<xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
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

    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">
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
	      <fo:table-cell font-size="6pt" display-align="center">
		<fo:block text-align="center">BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6</fo:block>
		<fo:block text-align="center">Tel.:  316.39.57, 316.05.78 Tel/Fax: 316.05.88,316.05.90</fo:block>
                <!--
		<fo:block text-align="center">Secretariat: Tel/Fax: 316.05.88; Tel: 316.39.57</fo:block>
		<fo:block text-align="center">Centrala: Tel: 316.05.78 Dep. Economic: Tel: 317.25.40</fo:block>
                -->
		<fo:block text-align="center">Magazin OBOR: sos. Mihai Bravu 6, Tel/Fax: 252.49.67</fo:block>
		<fo:block text-align="center">e-mail: conducere@setumsa.ro; desfacere@setumsa.ro</fo:block>
                <fo:block text-align="center">www.setumsa.ro</fo:block>
		<!--<fo:block text-align="center">Capital social: 14,10 miliarde lei</fo:block>-->
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>



    </fo:static-content>
    <!--/HEADER-->


    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="6pt"
		border-after-style="solid">Pagina <fo:page-number/></fo:block>
      <fo:block text-align="center" font-size="6pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">

      <!-- date generale despre oferta (numar, data, nume client, etc.) -->
      <fo:block font-size="8pt">
	<fo:table width="90%">
	  <fo:table-column column-number="1" column-width="25%"/>
	  <fo:table-column column-number="2" column-width="25%"/>
          <fo:table-column column-number="3" column-width="50%"/>


	  <fo:table-body>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="6pt" number-columns-spanned="2">
                 <fo:block>Catre: ${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="6pt">
                 <fo:block>De la: SC SETUM SA</fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="6pt" number-columns-spanned="2">
                 <fo:block>In atentia: </fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="6pt">
                 <fo:block>Nr oferta: ${doc["child::response/child::record/child::field[attribute::name='no']"]}/Data: ${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]}</fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="6pt" number-columns-spanned="1">
                 <fo:block>Tel:</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="2" border-style="solid" font-size="6pt" number-columns-spanned="1">
                 <fo:block>Fax:</fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="6pt">
                 <fo:block>Nr. pagini: <page-number-citation ref-id="last-page-block"/></fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="6pt" number-columns-spanned="2">
                 <fo:block>E-mail: </fo:block>
              </fo:table-cell>
              <fo:table-cell column-number="3" border-style="solid" font-size="6pt">
                 <fo:block>Referitor la cererea Dvs. de oferta nr: </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell column-number="1" border-style="solid" font-size="6pt" number-columns-spanned="3">
                 <fo:block>Prin prezenta va facem cunoscuta oferta noastra de pret pentru:</fo:block>
                 <fo:block>Obiectiv:</fo:block>
                 <fo:block>${doc["child::response/child::record/child::field[attribute::name='description']"]}</fo:block>
              </fo:table-cell>
            </fo:table-row>
	  </fo:table-body>
	</fo:table>
      </fo:block>


      <!-- liniile ofertei -->
      <fo:block font-size="8pt">

      <#assign lineno=1>
      <#assign lineoffers=group_line_offers(doc) >
      <#assign gcodes=lineoffers.usi?keys>
      <#list gcodes as code>
        <@display_usi lineno=lineno usi=lineoffers.usi[code]/>
	<#assign lineno += 1 >
      </#list>
      <@display_sisteme lineno=lineno sisteme=lineoffers.sisteme/>

      </fo:block>
      <!-- /liniile ofertei -->
 

      <!-- Empty block, for lata page number citation -->
      <fo:block id="last-page-block"></fo:block>
    </fo:flow>
    <!-- BODY -->