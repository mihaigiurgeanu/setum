<?xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-height="297mm"
			   page-width="210mm"
			   margin-top="20mm"
			   margin-bottom="10mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="24mm" margin-bottom="10mm"
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
	<fo:table border-collapse="collapse" table-layout="fixed" width="100%">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body>
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
	C-da ${doc["child::response/child::record/child::field[attribute::name='number']"]}
	din ${doc["child::response/child::record/child::field[attribute::name='date']"]}
	Pagina <fo:page-number/>
	din <fo:page-number-citation ref-id="last-page-block"/>
      </fo:block>
      <fo:block text-align="center" font-size="8pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">

      <fo:block font-size="10pt" font-family="Times">

      <!--
      <#assign lines=doc["response/record/field[attribute::name='lines']"] />
      <#assign usi=lines["record[field[attribute::name='product']/record/field[attribute::name='category.id']=9990]"] />
      <#assign invoices=doc["response/record/field[attribute::name='invoices']/record"] />
      <#assign nr_usi=usi?size />
      <#assign nr_crt_usa=0 />

      Nr linii: ${lines["record"]?size}
      Nr usi:   ${usi?size}
      
      <#assign moneda = doc["child::response/child::record/child::field[attribute::name='attribute5']"]>
      <#assign cursul = doc["child::response/child::record/child::field[attribute::name='attribute4']"]>
      
      <#if moneda?length == 0>
        <#assign moneda = "RON">
      </#if>
      <#if cursul?length == 0>
        <#assign cursul = "1">
      <#else>
      <#if cursul == "0">
        <#assign cursul = "1">
      </#if>
</#if>

      -->
      <!-- vor apare doar produsele de tip usa metalica customizata -->
      
      <#list usi as usa_line>
        <!--
        <#assign usa=usa_line["field[attribute::name='product']"] />
	<#assign nr_crt_usa=nr_crt_usa+1 />

	<#if nr_crt_usa<nr_usi><#assign pagebreak="always"/><#else><#assign pagebreak="auto"/></#if>
	Nr usa crt: ${nr_crt_usa}
	-->


	<!-- date generale despre comanda (numar, data, nume client, etc.) -->
	<fo:table width="100%"  border-collapse="collapse" table-layout="fixed" margin-bottom="1mm">
	  <fo:table-column column-number="1" column-width="50%" border-style="solid"/>
	  <fo:table-column column-number="2" column-width="50%" border-style="solid"/>

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1">
		<fo:block>
		  C-da nr: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='number']"]}</fo:inline>
		  / Data: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='date']"]}</fo:inline>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block>
		  Contract nr: <fo:inline font-weight="bold">${usa_line["field[attribute::name='contract']"]}</fo:inline>
		</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1">
		<fo:block>
		  Benef: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:inline>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block>
		  Tel/Fax: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientPhone']"]}</fo:inline> | Mobil: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='telefon']"]}</fo:inline>
		</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" number-columns-spanned="2">
		<fo:block>Adresa: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientCity']"]}, ${doc["child::response/child::record/child::field[attribute::name='clientAddress']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1">
		<fo:block>Cod fiscal: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientCompanyCode']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block>Nr. Reg. Com.: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientRegCom']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1">
		<fo:block>Cont IBAN: <fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientIban']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block>Banca:<fo:inline font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='clientBank']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" number-columns-spanned="2">
		<fo:block>Obiectiv: <fo:inline font-weight="bold">${usa_line["field[attribute::name='obiectiv']"]}</fo:inline></fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	  </fo:table-body>
	</fo:table>
	<!-- end date generale despre oferta -->



	<!-- Start usa ${usa.record["field[attribute::name='code']"]} -->
	<fo:block page-break-after="${pagebreak}"> <!-- blocul pentru o linie de comanda -->
	  <fo:table border-collapse="collapse" width="100%" table-layout="fixed" margin-bottom="1mm">
	    <fo:table-column column-number="1" column-width="25%" border-style="solid"/>
	    <fo:table-column column-number="2" column-width="25%" border-style="solid"/>
	    <fo:table-column column-number="3" column-width="50%" border-style="solid"/>
	    <fo:table-body>
	      <fo:table-row border-style="solid">
		<fo:table-cell column-number="1">
		  <fo:block>USA METALICA</fo:block>
		</fo:table-cell>
		<fo:table-cell column-number="2">
		  <fo:block>Nr. canate: <fo:inline font-weight="bold">${usa.record["field[attribute::name='k']"]}</fo:inline></fo:block>
		</fo:table-cell>
		<fo:table-cell column-number="3">
		  <fo:block>
		  Subcod: <fo:inline font-weight="bold">${ssearch(usa, "subclass")}
		  -
		  ${doc["child::response/child::record/child::field[attribute::name='agrementTehnic']"]}</fo:inline>
		  </fo:block>
		</fo:table-cell>
	      </fo:table-row>

	      <fo:table-row>
		<fo:table-cell column-number="1" number-columns-spanned="2">
		  <fo:block>Cod benef: <fo:inline font-weight="bold">${usa.record["field[attribute::name='name']"]}/${nr_crt_usa}</fo:inline></fo:block>
		</fo:table-cell>
		<fo:table-cell column-number="3">
		  <fo:block>Cod Setum: <fo:inline font-weight="bold">${usa.record["field[attribute::name='code']"]}/${nr_crt_usa}</fo:inline></fo:block>
		</fo:table-cell>
	      </fo:table-row>
	    </fo:table-body>
	  </fo:table>

	  <!-- tabel care imparte pagina in 2 coloane -->
	  <fo:table border-collapse="collapse" width="100%" border-style="none" table-layout="fixed" margin-bottom="1mm">
	    <fo:table-column column-number="1" column-width="50%"/>
	    <fo:table-column column-number="2" column-width="50%"/>
	    <fo:table-body>
	      <!-- 1. -->
	      <fo:table-row>
		<fo:table-cell column-number="2">
		  <!-- Termen livrare -->
		  <fo:table border-collapse="collapse" width="100%" border-style="solid" table-layout="fixed" margin-top="1mm" margin-bottom="1mm">
		    <fo:table-column column-number="1" column-width="50%" border-style="solid"/>
		    <fo:table-column column-number="2" column-width="50%" border-style="solid"/>
		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1"><fo:block>Termen livrare</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block text-align="right" font-weight="bold">${doc["child::response/child::record/child::field[attribute::name='termenLivarare']"][0]}</fo:block></fo:table-cell>			
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table>
		  <!-- sfarsit Termen livrare -->
		</fo:table-cell>
	      </fo:table-row>
	      <!-- 2. -->
	      <!-- dimensiuni/varianta/finisaj/tip canate/toc-prag -->
	      <fo:table-row>
		<!-- dimensiuni/vairianta/tip canate/toc-prag -->
		<fo:table-cell column-number="1" margin-right="1mm">
		  <!-- dimensiuni/varianta -->
		  <fo:block border-bottom-style="solid" font-size="11pt" font-weight="bold">Lexe = ${usa.record["field[attribute::name='lexec']"]}; Hexe = ${usa.record["field[attribute::name='hexec']"]}</fo:block>

		  <fo:table width="100%" border-style="none" table-layout="fixed">
		    <fo:table-column column-number="1" column-width="25%"/>
		    <fo:table-column column-number="2" column-width="75%"/>
		    <fo:table-body>
		      <fo:table-cell>
			<fo:block border-style="solid">
			  <fo:block border-bottom-style="solid" text-align="center">Dimensiuni executie</fo:block>
			  <fo:block border-bottom-style="solid">Le = <fo:inline font-weight="bold">${usa.record["field[attribute::name='le']"]}</fo:inline></fo:block>
			  <fo:block>He = <fo:inline font-weight="bold">${usa.record["field[attribute::name='he']"]}</fo:inline></fo:block>
			</fo:block>
		      </fo:table-cell>
		      <fo:table-cell>
			<fo:table width="100%" table-layout="fixed" border-collapse="collapse" border-style="solid" margin-right="1mm">
			  <fo:table-column column-number="1" border-style="solid" column-width="20%"/>
			  <fo:table-column column-number="2" border-style="solid" column-width="80%"/>
			      
			  <fo:table-body>
			    <!-- Varianta -->
			    <fo:table-row>
			      <fo:table-cell column-number="1" number-columns-spanned="2">
				<fo:block text-align="center">Varianta</fo:block>
			      </fo:table-cell>
			    </fo:table-row>
			    <fo:table-row>
			      <fo:table-cell column-number="1" number-columns-spanned="2">
				<fo:block font-weight="bold">
				  <!--${usa.record["field[attribute::name='version']"]}-->
				  <!--${usa.record["field[attribute::name='k']"]}K-->
				  <!--${usa.record["field[attribute::name='subclass']"]}-->
				  <!-- ${ssearch(usa, 'subclass')} - -->
				  ${ssearch(usa, 'version')}
				</fo:block>
			      </fo:table-cell>
			    </fo:table-row>
			    <!-- /Varianta -->

			    <!-- Buc, Sens deschidere -->
			    <fo:table-row border-style="solid">
			      <fo:table-cell column-number="1">
				<fo:block text-align="center">Buc</fo:block>
			      </fo:table-cell>
			      <fo:table-cell column-number="2" padding-left="1mm">
				<fo:block>Sens deschidere</fo:block>
			      </fo:table-cell>
			    </fo:table-row>
			    <fo:table-row>
			      <fo:table-cell><fo:block text-align="center" font-weight="bold">
				${usa_line["field[attribute::name='quantity']"]}</fo:block></fo:table-cell>
			      <fo:table-cell  padding-left="1mm">
				<fo:block font-weight="bold">
				  ${ssearch(usa, "openingSide")}
				  ${ssearch(usa, "openingDir")}
				</fo:block>
			      </fo:table-cell>
			    </fo:table-row>
			    <!-- /Buc, Sens deschidere -->

			  </fo:table-body>
			</fo:table>
			<!-- /Varianta, Bucati, Sens deschider -->


		      </fo:table-cell>
		    </fo:table-body>
		  </fo:table>
		  <#if usa.record["field[attribute::name='k']"] = "2" >
		  <!-- tip canate -->
		  <fo:table border-style="solid" border-collapse="collapse" table-layout="fixed" width="100%" margin-top="1mm">
		    <fo:table-column column-width="20%" column-number="1" border-style="solid"/>
		    <fo:table-column column-width="80%" column-number="2" border-style="solid"/>
		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-rows-spanned="2">
			  <fo:block text-align="center">TIP CANATE</fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2">
			  <fo:block font-weight="bold">
			    <#assign kType = usa.record["field[attribute::name='kType']"]["@@text"]>
			    <#if kType = "1">
			    EGALE
			    <#elseif kType = "2">
			    INEGALE
			    <#else>
			    - <!-- kType necunoscut: ${kType} -->
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      <fo:table-row>
			<fo:table-cell column-number="2">
			  <fo:block>Lcurenta = <fo:inline font-weight="bold">${usa.record["field[attribute::name='lCurrent']"]}</fo:inline></fo:block>
			</fo:table-cell>
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table>
		  </#if>
		  <!-- toc, prag -->
		  <fo:table border-style="solid" border-collapse="collapse" table-layout="fixed" width="100%" margin-top="1mm">
		    <fo:table-column border-style="solid" column-number="1" column-width="25%"/>
		    <fo:table-column border-style="solid" column-number="2" column-width="25%"/>
		    <fo:table-column border-style="solid" column-number="3" column-width="25%"/>
		    <fo:table-column border-style="solid" column-number="4" column-width="25%"/>
		    
		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-rows-spanned="2">
			  <fo:block>Toc</fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" number-columns-spanned="3">
			  <fo:block font-weight="bold">${ssearch(usa, 'frameType')}</fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='frameType']"] != "1">ltoc= ${usa.record["field[attribute::name='lFrame']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='frameType']"] != "1">btoc= ${usa.record["field[attribute::name='bFrame']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="4">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='frameType']"] != "1">ctoc= ${usa.record["field[attribute::name='cFrame']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      
		      
		      
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-rows-spanned="2">
			  <fo:block>Prag</fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" number-columns-spanned="3">
			  <fo:block font-weight="bold">
			  ${ssearch(usa, 'tresholdType')}
			  <#if usa.record["field[attribute::name='tresholdType']"] == "3">
			  <#if usa.record["field[attribute::name='tresholdSpace']"] == "1">
			  cu incastrare
			  h incastrare = ${usa.record["field[@name='h1Treshold']"]}
			  <#else>
			  fara incastrare
			  h = ${usa.record["field[@name='h2Treshold']"]}
			  </#if>
			  </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='tresholdType']"] == "2">lprag= ${usa.record["field[attribute::name='lTreshold']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='tresholdType']"] == "2">cprag= ${usa.record["field[attribute::name='cTreshold']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="4">
			  <fo:block text-align="center" font-weight="bold"><#if usa.record["field[attribute::name='tresholdType']"] == "2">hprag= ${usa.record["field[attribute::name='hTreshold']"]}<#else>-</#if></fo:block>
			</fo:table-cell>
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table>
		</fo:table-cell>

		<!-- finisaj -->
		<fo:table-cell margin-left="1mm">
		  <fo:block border-top-style="solid" border-left-style="solid" border-right-style="solid">
		    <fo:block border-bottom-style="solid" text-align="center">Finisaj</fo:block>

			  <#if (usa.record["field[attribute::name='intFinisajTocId']"] = "0" ||
			        usa.record["field[attribute::name='finisajTocBlat']"] = "true") &&
			       (usa.record["field[attribute::name='intFinisajGrilajId']"] = "0" ||
			        usa.record["field[attribute::name='finisajGrilajBlat']"] = "true") &&
			       (usa.record["field[attribute::name='intFinisajFereastraId']"] = "0" ||
			        usa.record["field[attribute::name='finisajFereastraBlat']"] = "true") &&
			       (usa.record["field[attribute::name='intFinisajSupraluminaId']"] = "0" ||
			        usa.record["field[attribute::name='finisajSupraluminaBlat']"] = "true") &&
			       (usa.record["field[attribute::name='intFinisajPanouLateralId']"] = "0" ||
			        usa.record["field[attribute::name='finisajPanouLateralBlat']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajBlatId']"] = "0" ||
			        usa.record["field[attribute::name='finisajBlatExtInt']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajTocId']"] = "0" ||
			        usa.record["field[attribute::name='finisajTocExtInt']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajGrilajId']"] = "0" ||
			        usa.record["field[attribute::name='finisajGrilajExtInt']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajFereastraId']"] = "0" ||
			        usa.record["field[attribute::name='finisajFereastraExtInt']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajSupraluminaId']"] = "0" ||
			        usa.record["field[attribute::name='finisajSupraluminaExtInt']"] = "true") &&
			       (usa.record["field[attribute::name='extFinisajPanouLateralId']"] = "0" ||
			        usa.record["field[attribute::name='finisajPanouLateralExtInt']"] = "true")>

				<@finisaj name="intFinisajBlat"></@finisaj>
			  <#else>
			  <#if usa.record["field[attribute::name='finisajTocBlat']"] = "true" &&
			       usa.record["field[attribute::name='finisajBlatExtInt']"] = "true" &&
			       usa.record["field[attribute::name='finisajTocExtInt']"] = "true"
			  >
			     <@finisaj name="intFinisajBlat">Toc/Blat (interior/exterior)</@finisaj>
			  
			  <#elseif (usa.record["field[attribute::name='finisajBlatExtInt']"] = "true") &&
			           (usa.record["field[attribute::name='finisajTocExtInt']"] = "true") >
			     <@finisaj name="intFinisajBlat">Blat (interior/exterior)</@finisaj>
			     <@finisaj name="intFinisajToc">Toc (interior/exterior)</@finisaj>
			  
			  <#elseif usa.record["field[attribute::name='finisajBlatExtInt']"] = "true">
			     <@finisaj name="intFinisajBlat">Blat (interior/exterior)</@finisaj>
			     <@finisaj name="intFinisajToc">Toc (interior)</@finisaj>
			     <@finisaj name="extFinisajToc">Toc (exterior)</@finisaj>
			  <#elseif usa.record["field[attribute::name='finisajTocExtInt']"] = "true">
			     <@finisaj name="intFinisajBlat">Blat (interior)</@finisaj>
			     <@finisaj name="extFinisajBlat">Blat (exterior)</@finisaj>
			     <@finisaj name="intFinisajToc">Toc (interior/exterior)</@finisaj>
			  <#else>
			     <@finisaj name="intFinisajBlat">Blat (interior)</@finisaj>
			     <@finisaj name="extFinisajBlat">Blat (exterior)</@finisaj>
			     <@finisaj name="intFinisajToc">Toc (interior)</@finisaj>
			     <@finisaj name="extFinisajToc">Toc (exterior)</@finisaj>
			  </#if>
			     <@finisaj name="intFinisajGrilaj">Grilaj (interior)</@finisaj>
			     <@finisaj name="extFinisajGrilaj">Grilaj (exterior)</@finisaj>
			     <@finisaj name="intFinisajFereastra">Fereastra (interior)</@finisaj>
			     <@finisaj name="extFinisajFereastra">Fereastra (exterior)</@finisaj>
			     <@finisaj name="intFinisajSupralumina">Supralumina (interior)</@finisaj>
			     <@finisaj name="extFinisajSupralumina">Supralumina (exterior></@finisaj>
			     <@finisaj name="intFinisajPanouLateral">Panou lateral (interior)</@finisaj>
			     <@finisaj name="extFinisajPanouLateral">Panou lateral (exterior)</@finisaj>
			  </#if>
		    </fo:block>
		    <!-- end finisaje -->

		  <!-- sisteme inchidere -->
		  <!-- Sisteme -->
		  <fo:table border-collapse="collapse" table-layout="fixed" space-after="1mm" width="100%">
		    <fo:table-column column-number="1" column-width="22%" border-style="solid"/>
		    <fo:table-column column-number="2" column-width="66%" border-style="solid"/>
		    <fo:table-column column-number="3" column-width="12%" border-style="solid"/>

		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="3">
			  <fo:block>
			    <!-- <#assign montare_sistem = usa.record["field[attribute::name='montareSistem']"]> -->
			    <#if montare_sistem = "2">Fara montare sisteme<#else>Montare sisteme:</#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>

		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="3"><fo:block>SISTEME DE INCHIDERE: SETUM</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2"><fo:block text-align="center">COD</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block text-align="center">BUC</fo:block></fo:table-cell>
		      </fo:table-row>
		      <#assign decupareSistemId = usa.record["field[attribute::name='decupareSistemId']"]>
		      <#if decupareSistemId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2">
			  <fo:block font-weight="bold">
			    ${ssearch(usa, 'decupareSistemId')}
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3">
			  <fo:block font-weight="bold">
			    1
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign broascaId = usa.record["field[attribute::name='broascaId']"]>
		      <#if broascaId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Broasca:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2">
			  <fo:block font-weight="bold">
			    <#if broascaId != "0">
			    ${ssearch(usa, 'broascaId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3">
			  <fo:block font-weight="bold">
			    <#if broascaId != "0">
			    ${usa.record["field[attribute::name='broascaBuc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign cilindruId = usa.record["field[attribute::name='cilindruId']"]>
		      <#if cilindruId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Cilindru:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if cilindruId != "0">
			    ${ssearch(usa, 'cilindruId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if cilindruId != "0">
			    ${usa.record["field[attribute::name='cilindruBuc']"]}
			    </#if>			    
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign copiatCheieId = usa.record["field[attribute::name='copiatCheieId']"]>
		      <#if copiatCheieId != "0" >
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Multiplicat chei:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if copiatCheieId != "0">
			    ${ssearch(usa, 'copiatCheieId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if copiatCheieId != "0">
			    ${usa.record["field[attribute::name='copiatCheieBuc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign vizorId = usa.record["field[attribute::name='vizorId']"]>
		      <#if vizorId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Vizor:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if vizorId != "0">
			    ${ssearch(usa, 'vizorId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if vizorId != "0">
			    ${usa.record["field[attribute::name='vizorBuc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign sildId = usa.record["field[attribute::name='sildId']"]>
		      <#assign rozetaId = usa.record["field[attribute::name='rozetaId']"]>
		      <#assign manerId = usa.record["field[attribute::name='manerId']"]>
		      <#if sildId != "0" || rozetaId != "0" || manerId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Silduri / Rozete + Maner:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if sildId != "0">
			    ${ssearch(usa, 'sildId')}
			    </#if>
			    <#if rozetaId != "0">
			    ${ssearch(usa, 'rozetaId')}
			    </#if>
			    <#if manerId != "0">
			    ${ssearch(usa, 'manerId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if sildId != "0">
			    ${usa.record["field[attribute::name='sildBuc']"]}
			    <#elseif rozetaId != "0">
			    ${usa.record["field[attribute::name='rozetaBuc']"]}
			    <#elseif manerId != "0">
			    ${usa.record["field[attribute::name='manerBuc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign yallaId = usa.record["field[attribute::name='yalla1Id']"]>
		      <#if yallaId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Yalla:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if yallaId != "0">
			    ${ssearch(usa, 'yalla1Id')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if yallaId != "0">
			    ${usa.record["field[attribute::name='yalla1Buc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign yalla2Id = usa.record["field[attribute::name='yalla2Id']"]>
		      <#if yalla2Id != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Yalla:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if yalla2Id != "0">
			    ${ssearch(usa, 'yalla2Id')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if yalla2Id != "0">
			    ${usa.record["field[attribute::name='yalla2Buc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign amortizorId = usa.record["field[attribute::name='amortizorId']"]>
		      <#if amortizorId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Amortizor:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if amortizorId != "0">
			    ${ssearch(usa, 'amortizorId')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if amortizorId != "0">
			    ${usa.record["field[attribute::name='amortizorBuc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign baraAntipanicaId = usa.record["field[attribute::name='baraAntipanicaId']"]>
		      <#if baraAntipanicaId != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Bara antipanica:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    ${ssearch(usa, 'baraAntipanicaId')}
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    ${usa.record["field[attribute::name='baraAntipanicaBuc']"]}
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign alteSisteme1Id = usa.record["field[attribute::name='alteSisteme1Id']"]>
		      <#if alteSisteme1Id != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Alte sisteme:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if alteSisteme1Id != "0">
			    ${ssearch(usa, 'alteSisteme1Id')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if alteSisteme1Id != "0">
			    ${usa.record["field[attribute::name='alteSisteme1Buc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <#assign alteSisteme2Id = usa.record["field[attribute::name='alteSisteme2Id']"]>
		      <#if alteSisteme2Id != "0">
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1">
			  <fo:block>
			    Alte sisteme:
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" font-weight="bold">
			  <fo:block>
			    <#if alteSisteme2Id != "0">
			    ${ssearch(usa, 'alteSisteme2Id')}
			    </#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3" font-weight="bold">
			  <fo:block>
			    <#if alteSisteme2Id != "0">
			    ${usa.record["field[attribute::name='alteSisteme2Buc']"]}
			    </#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      </#if>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2"><fo:block>SISTEME DE INCHIDERE: BENEFICIAR</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1"><fo:block text-align="center">Descriere/Tip</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block text-align="center">BUC</fo:block></fo:table-cell>
		      </fo:table-row>
		      <@benefs name="broasca">broasca</@benefs>
		      <@benefs name="cilindru">cilindru</@benefs>
		      <@benefs name="sild">sild</@benefs>
		      <@benefs name="yalla">yalla</@benefs>
		      <@benefs name="vizor">vizor</@benefs>
		      <@benefs name="baraAntipanica">bara antipanica</@benefs>
		      <@benefs name="maner">maner</@benefs>
		      <@benefs name="selectorOrdine">selector ordine</@benefs>
		      <@benefs name="amortizor">amortizor</@benefs>
		      <@benefs name="alteSisteme1"></@benefs>
		      <@benefs name="alteSisteme2"></@benefs>
		    </fo:table-body>
		  </fo:table>
		  <!-- end sisteme inchidere -->


		</fo:table-cell>
	      </fo:table-row>
	      <!-- 3. -->
	      <!-- canate -->
	      <fo:table-row>
		<!-- canat1 -->
		<fo:table-cell column-number="1" padding-right="1mm" padding-top="1mm">
		  <!-- canat1 -->
		  <#macro canat k>
		  <!-- tip foaie -->
		  <fo:table border-collapse="collapse" width="100%" table-layout="fixed" space-after="1mm">
		    <fo:table-column column-number="1" column-width="16%" border-style="solid"/>
		    <fo:table-column column-number="2" column-width="42%" border-style="solid"/>
		    <fo:table-column column-number="3" column-width="42%" border-style="solid"/>

		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-rows-spanned="3">
			  <fo:block text-align="center" padding-top="20%">TIP FOAIE</fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2" number-columns-spanned="2">
			  <fo:block>
			    <#if k = 1>
			    Canat Prinicpal K1
			    <#else>
			    Canat Secundar K2
			    </#if>			    
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2"><fo:block>La interior</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block>La exterior</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2">
			  <fo:block font-weight="bold">
			    <#if k=1>${ssearch(usa, "intFoil")}<#else>${ssearch(usa, "intFoilSec")}</#if>
			  </fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="3">
			  <fo:block font-weight="bold">
			    <#if k=1>${ssearch(usa, "extFoil")}<#else>${ssearch(usa, "extFoilSec")}</#if>
			  </fo:block>
			</fo:table-cell>
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table><!--/tip foaie-->
		  <!-- /canat${k} -->
		  </#macro>
		  <@canat k=1/>

		  

		 
		  


		</fo:table-cell>

		<!-- canat2 -->
		<fo:table-cell column-number="2" padding-left="1mm" padding-top="1mm">
		  <#if usa.record["field[attribute::name='k']"] = "2" >
		  <!-- canat2 -->
		  <@canat k=2 />
		  <!-- end canat2 -->
		  </#if>
		  <fo:block></fo:block>
		</fo:table-cell>
	      </fo:table-row>


	      <!--
		      Selectie optiuni:
		      <#assign ferestre=usa.record["field[attribute::name='parts']/record/field[attribute::name='part'][record[field[attribute::name='businessCategory']='http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra']]"]><#t>
		      Nr ferestre: ${ferestre?size}
		      <#t>
		      <#assign panouri=usa.record["field[attribute::name='parts']/record/field[attribute::name='part'][record[field[attribute::name='businessCategory']='http://www.kds.ro/erp/businessCategory/setum/optiuni/panoulateral']]"]><#t>
		      Nr panouri: ${panouri?size}
		      <#t>
		      <#assign supralumini=usa.record["field[attribute::name='parts']/record/field[attribute::name='part'][record[field[attribute::name='businessCategory']='http://www.kds.ro/erp/businessCategory/setum/optiuni/supralumina']]"]><#t>
		      <#t>
		      Nr supralumini: ${supralumini?size}
		      <#t>
		      <#assign optiuni = panouri + supralumini><#t>
		      <#if optiuni?size &gt; 1>
		        <#assign optiuni_st = optiuni[0..(((optiuni?size))/2?int)-1]>
			<#assign optiuni_dr = optiuni[(((optiuni?size))/2?int)..]>
		      <#else>
		        <#assign optiuni_st = optiuni>
			<#assign optiuni_dr = []>
		      </#if>
		      Nr optiuni stanga:  ${optiuni_st?size}
		      Nr optiuni dreapta: ${optiuni_dr?size}
		      <#assign optiuni = ferestre + panouri + supralumini><#t>
	      -->


	      <!-- 3 bis -->
	      <#macro list_optiune optiune>
	            <@enum_init sep="/"/>
	            <fo:block border-top-style="solid">
		      <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra">
		      <!--fereastra-->
		      <fo:block>
		      <#local fk_deschidere = optiune.record["field[@name='deschidere']"]><#t>
		      <#local fk_tipGeam = optiune.record["field[@name='tipGeam']"]><#t>
		      <#local fk_tipGrilaj = optiune.record["field[@name='tipGrilaj']"]><#t>
		      
		      <@enum_next/>
		      <fo:inline font-weight="bold">Fereastra</fo:inline> <!--${ssearch(optiune, "standard")}-->
		      Lf x Hf =  <fo:inline font-weight="bold">${optiune.record["field[@name='lf']"]} x ${optiune.record["field[@name='hf']"]}</fo:inline>
		      <!--deschidere-->
		      <@enum_next/>
		      <#if fk_deschidere = "1">Fixa
		      <#else>
		      Mobila 
		      <@enum_next/>
		      Sens deschidere:  <fo:inline font-weight="bold">${ssearch(optiune, "sensDeschidere")} </fo:inline>
		      <@enum_next/>
		      Balamale:  <fo:inline font-weight="bold">${ssearch(optiune, "pozitionareBalamale")}</fo:inline>
		      </#if><!--/deschidere-->

		       <fo:inline font-weight="bold">
		      ${optiune.record["field[attribute::name='pozitionare1']"]}
		      ${optiune.record["field[@name='pozitionare2']"]}
		      ${optiune.record["field[@name='pozitionare3']"]}
		       </fo:inline>

		      <#if fk_tipGeam = "1">
		        <@enum_next/>
		        Geam  <fo:inline font-weight="bold">${ssearch(optiune, "geamSimpluId")}</fo:inline>
		      <#elseif fk_tipGeam = "2">
		        <@enum_next/>
			Geam  <fo:inline font-weight="bold">${ssearch(optiune, "geamTermopanId")}</fo:inline>
		      </#if>

		      <#if fk_tipGrilaj = "1">
		        <@enum_next/>
		        Grilaj  <fo:inline font-weight="bold">${ssearch(optiune, "grilajStasId")}</fo:inline>
		      <#elseif fk_tipGrilaj = "2">
		        <@enum_next/>
		        Grilaj  <fo:inline font-weight="bold">atipic</fo:inline>
		      </#if>
		      -  <fo:inline font-weight="bold">${optiune.record["field[@name='quantity']"]}</fo:inline> buc
		      </fo:block><!--/fereastra-->
		      </#if>


		      <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/panoulateral">
		      <!-- Panou lateral -->
		      <fo:block>
		      <@enum_next/>
		      <fo:inline font-weight="bold">Panou lateral</fo:inline>
		      
		      <@enum_next/>
		      Lp x Hp =
		       <fo:inline font-weight="bold">
			 ${optiune.record["field[attribute::name='lpl']"]} x
			 ${optiune.record["field[attribute::name='hpl']"]}
		       </fo:inline>

		      <#local nr_celule = optiune.record["field[attribute::name='cells']"]>
		      <#if nr_celule != "1">
		      <@enum_next/>
		      Nr celule:  <fo:inline font-weight="bold">${nr_celule}</fo:inline>
		      </#if>

		      <#local o_deschidere = optiune.record["field[attribute::name='deschidere']"]>
		      <!--deshidere-->
		      <@enum_next/>
		      <#if o_deschidere = "1"> <fo:inline font-weight="bold">Fix</fo:inline>
		      <#else>
		       <fo:inline font-weight="bold">Mobil</fo:inline>
		      <@enum_next/>
		      Deschidere  <fo:inline font-weight="bold">${ssearch(optiune, "sensDeschidere")}</fo:inline>
		      <@enum_next/>
		      Balamale  <fo:inline font-weight="bold">${ssearch(optiune, "pozitionareBalamale")}</fo:inline>
		      </#if><!--deschidere-->
		      
		      <@enum_next/>
		      Componenta:  <fo:inline font-weight="bold">${ssearch(optiune, "componenta")}</fo:inline>
		      <#local o_tipGeam = optiune.record["field[attribute::name='tipGeam']"]> 
		      <#local o_tipTabla = optiune.record["field[attribute::name='tipTabla']"]>

		      <#if o_tipGeam = "1">
		        <@enum_next/>
		        Geam  <fo:inline font-weight="bold">${ssearch(optiune, "geamSimpluId")}</fo:inline>
		      <#elseif o_tipGeam = "2">
		        <@enum_next/>
		        Geam  <fo:inline font-weight="bold">${ssearch(optiune, "geamTermopanId")}</fo:inline>
		      </#if>
		      <#if o_tipTabla != "0">
		      <@enum_next/>
		       <fo:inline font-weight="bold">${ssearch(optiune, "tipTabla")} ${ssearch(optiune, "tablaId")}</fo:inline>
		      </#if>

		      <#local o_tipGrilaj = optiune.record["field[attribute::name='tipGrilaj']"]>
		      <#if o_tipGrilaj = "1">
		      <@enum_next/>
		      Grilaj  <fo:inline font-weight="bold">${ssearch(optiune, "grilajStasId")}</fo:inline>
		      <#elseif o_tipGrilaj = "2">
		      <@enum_next/>
		      Grilaj  <fo:inline font-weight="bold">atipic</fo:inline>
		      </#if>

		      -  <fo:inline font-weight="bold">${optiune.record["field[attribute::name='quantity']"]}</fo:inline> buc
		      </fo:block><!--/Panou lateral -->
		      </#if>


		      <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/supralumina">
		      <!--Supralumina-->
		      <fo:block>
		      <@enum_next/>
		      <fo:inline font-weight="bold">Supralumina</fo:inline> tip ${ssearch(optiune, "tip")}
		      <@enum_next/>
		      Ls x Hs =  <fo:inline font-weight="bold">
		      ${optiune.record["field[attribute::name='ls']"]} x
		      ${optiune.record["field[attribute::name='hs']"]}
		      </fo:inline>

		      <#if optiune.record["field[attribute::name='cells']"] != "1">
		      <@enum_next/>
		      Nr celule:  <fo:inline font-weight="bold">${optiune.record["field[attribute::name='cells']"]}</fo:inline>
		      </#if>

		      <#local o_deschidere = optiune.record["field[attribute::name='deschidere']"]>
		      <!--deschidere-->
		      <@enum_next/>
		      <#if o_deschidere = "1">Fixa
		      <#else>
		      Mobila
		      <@enum_next/>
		      Deschidere  <fo:inline font-weight="bold">${ssearch(optiune, "sensDeschidere")}</fo:inline>
		      <@enum_next/>
		      Balamale  <fo:inline font-weight="bold">${ssearch(optiune, "pozitionareBalamale")}</fo:inline>
		      </#if><!--/deschidere-->

		      <@enum_next/>
		      Componenta  <fo:inline font-weight="bold">${ssearch(optiune, "componenta")}</fo:inline>
		      <#local o_tipGeam = optiune.record["field[attribute::name='tipGeam']"]> 
		      <#local o_tipTabla = optiune.record["field[attribute::name='tipTabla']"]>
		      <#if o_tipGeam = "1">
		      <@enum_next/>
		       <fo:inline font-weight="bold">${ssearch(optiune, "geamSimpluId")}</fo:inline>
		      <#elseif o_tipGeam = "2">
		      <@enum_next/>
		       <fo:inline font-weight="bold">${ssearch(optiune, "geamTermopanId")}</fo:inline>
		      </#if>
		      <#if o_tipTabla != "0">
		      <@enum_next/>
		       <fo:inline font-weight="bold">${ssearch(optiune, "tipTabla")} ${ssearch(optiune, "tablaId")}</fo:inline>
		      </#if>

		      <#local o_tipGrilaj = optiune.record["field[attribute::name='tipGrilaj']"]>
		      <#if o_tipGrilaj = "1">
		      <@enum_next/>
		      Grilaj  <fo:inline font-weight="bold">${ssearch(optiune, "grilajStasId")}</fo:inline>
		      <#elseif o_tipGrilaj = "2">
		      <@enum_next/>
		      Grilaj atipic
		      </#if>
		      -  <fo:inline font-weight="bold">${optiune.record["field[@name='quantity']"]}</fo:inline> buc
		      </fo:block><!--/Supralumina-->
		      </#if>
		    </fo:block>
	      </#macro><#-- list_optiune -->

	      <fo:table-row>
		<fo:table-cell number-columns-spanned="2">
		  <!--Optiuni-->
		  <#if usa.record["field[attribute::name='k']"] = "1">
		  <!-- pentru usile cu un canat am doar un bloc de optiuni -->
		  <fo:block border-style="solid" margin-top="1mm" margin-bottom="1mm">
		    <fo:inline font-weight="bold">Optiuni</fo:inline>
		    <#list optiuni as o>
		    <@list_optiune optiune=o />
		    </#list>
		  </fo:block>
		  <#else> <!--  usa.record["field[attribute::name='k']"] = "2" -->
		  <!-- pentru usile in doua canate, sunt 3 blocuri de optiuni:
		       optiuni canat principal (fereastra)
		       optiuni canat secundar  (fereastra)
		       optiuni in general
		  -->
		  <#assign ferestre_k1=usa.record["field[attribute::name='parts']/record/field[attribute::name='part'][record[field[attribute::name='businessCategory']='http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra'][field[attribute::name='canat']='1']]"]><#t>
		  <!-- Nr ferestre k1:  ${ferestre_k1?size} -->
		  <#assign ferestre_k2=usa.record["field[attribute::name='parts']/record/field[attribute::name='part'][record[field[attribute::name='businessCategory']='http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra'][field[attribute::name='canat']='2']]"]><#t>
		  <!-- Nr ferestre k2:  ${ferestre_k2?size} -->
		  <#if ferestre_k1?size &gt; 0>
		  <fo:block border-style="solid" margin-top="1mm" margin-bottom="1mm">
		    <fo:inline font-weight="bold">Optiuni canat principal K1</fo:inline>
		    <#list ferestre_k1 as o>
		    <@list_optiune optiune=o />
		    </#list>
		  </fo:block>
		  </#if><!-- /optiuni canat principal-->
		  <#if ferestre_k2?size &gt; 0>
		  <fo:block border-style="solid" margin-top="1mm" margin-bottom="1mm">
		    <fo:inline font-weight="bold">Optiuni canat secundar K2</fo:inline>
		    <#list ferestre_k2 as o>
		    <@list_optiune optiune=o />
		    </#list>
		  </fo:block>
		  </#if> <!-- /optiuni canat secundar -->
		  <#if panouri?size &gt; 0 || supralumini?size &gt; 0>
		  <fo:block border-style="solid" margin-top="1mm" margin-bottom="1mm">
		    <fo:inline font-weight="bold">Optiuni</fo:inline>
		    <#list panouri as o>
		    <@list_optiune optiune=o />
		    </#list>
		    <#list supralumini as o>
		    <@list_optiune optiune=o />
		    </#list>
		  </fo:block>
		  </#if><!-- /optiuni independente de canat -->



		  </#if>
		  <!--/Optiuni-->

		  <!-- masca/lacrimar/bolturi/platbanda -->
		  <@enum_init/>
		  <#assign masca =	usa.record["field[@name='masca']"]><#t>
		  <#assign lacrimar =	usa.record["field[@name='lacrimar']"]><#t>
		  <#assign bolturi =	usa.record["field[@name='bolturi']"]><#t>
		  <#assign platbanda =  usa.record["field[@name='platbanda']"]><#t>
		  <#if masca != "0" || lacrimar != "0" || bolturi != "0" || platbanda != "0">
		  <fo:block border-style="solid" margin-top="1mm" margin-bottom="1mm">
		    <#if masca		!= "0"><@enum_next/>Masca =  <fo:inline font-weight="bold">${ssearch(usa, "masca")}</fo:inline></#if>
		    <#if lacrimar	!= "0"><@enum_next/>Lacrimar =  <fo:inline font-weight="bold">${ssearch(usa, "lacrimar")}</fo:inline></#if>
		    <#if bolturi	!= "0"><@enum_next/>Bolturi =  <fo:inline font-weight="bold">${ssearch(usa, "bolturi")}</fo:inline></#if>
		    <#if platbanda	!= "0"><@enum_next/>Platbanda =  <fo:inline font-weight="bold">${ssearch(usa, "platbanda")}</fo:inline></#if>
		    
		  </fo:block>
		  </#if>
		  <!-- /masca/lacrimar/bolturi/platbanda -->


		  <!-- Observatii -->
		  <fo:block border-style="solid" margin-bottom="1mm" linefeed-treatment="preserve">Observatii usa:  <fo:inline font-weight="bold">${usa.record["field[attribute::name='sistemeComment']"]}</fo:inline></fo:block>
		</fo:table-cell>
	      </fo:table-row>



	      <!-- 4. -->
	      <!-- montaj/localitate -->
	      <fo:table-row>
		<fo:table-cell number-columns-spanned="2">
		  <fo:table border-collapse="collapse" width="100%" table-layout="fixed" margin-bottom="1mm">
		    <fo:table-column column-number="1" border-style="solid" column-width="15%"/>
		    <fo:table-column column-number="2" border-style="solid" column-width="35%"/>
		    <fo:table-column column-number="3" border-style="solid" column-width="15%"/>
		    <fo:table-column column-number="4" border-style="solid" column-width="35%"/>
		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1"><fo:block>Tip montaj</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block font-weight="bold">${search(lines, "montajId", usa_line["field[attribute::name='montajId']"])}</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block>Localitatea</fo:block></fo:table-cell>
			<fo:table-cell column-number="4"><fo:block font-weight="bold"><!--${search(lines, "locationId", usa_line["field[attribute::name='locationId']"])}-->${usa_line["field[attribute::name='otherLocation']"]}</fo:block></fo:table-cell>
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table>

		</fo:table-cell>
	      </fo:table-row>
     
	      <!-- 5. -->
	      <!-- explicatie pret -->
	      <fo:table-row>
		<!-- pret -->
		<fo:table-cell padding-right="1mm">
		  <fo:table border-collapse="collapse" width="100%" margin-bottom="1mm">
		    <fo:table-column column-number="1" column-width="30%" border-style="solid"/>
		    <fo:table-column column-number="2" column-width="35%" border-style="solid"/>
		    <fo:table-column column-number="3" column-width="35%" border-style="solid"/>
		    <fo:table-body>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-rows-spanned="3">
			  <fo:block text-align="center" padding-top="22%">PRET</fo:block>
			</fo:table-cell>
			<fo:table-cell column-number="2"><fo:block>Valoare/buc</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block>Valoare/total</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2"><fo:block font-weight="bold">[${moneda}]</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block font-weight="bold">[${moneda}]</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="2"><fo:block>fara TVA</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block>fara TVA</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1"><fo:block>USA</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='price']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='value']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1"><fo:block>MONTAJ</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='montajUnitar']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='valoareMontaj']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2"><fo:block>TRANSPORT</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='valoareTransport']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2"><fo:block text-align="right">Valoare/total(fara TVA)</fo:block></fo:table-cell>
			<fo:table-cell column-number="3"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='lineValue']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
		      </fo:table-row>
		      <fo:table-row border-style="solid">
			<fo:table-cell column-number="1" number-columns-spanned="2"><fo:block text-align="right">Valoare/total(cu TVA)</fo:block></fo:table-cell>
			<fo:table-cell column-number="2"><fo:block font-weight="bold" text-align="right">${(cvnumber(usa_line["field[attribute::name='lineTotal']"])/(cursul?number))?string("#,##0.00")}</fo:block></fo:table-cell>
		      </fo:table-row>
		    </fo:table-body>
		  </fo:table>
		</fo:table-cell> <!-- /pret -->

		
	      </fo:table-row>
	    </fo:table-body>
	  </fo:table>
	  <!-- sfarsit impartire pagina in 2 coloane -->

	  
	</fo:block><!-- sfarsit bloc linie de comanda -->
	<!-- End usa ${usa.record["field[attribute::name='code']"]} -->

      </#list>

      <!-- Observatii montaj -->
      <#if (doc.response.record["field[attribute::name='observatii']"])?length &gt; 0>
      <fo:block border-style="solid" margin-bottom="1mm" linefeed-treatment="preserve">Observatii montaj:  <fo:inline font-weight="bold">${doc.response.record["field[attribute::name='observatii']"]}</fo:inline></fo:block>
      </#if>	 
      <!-- /Observatii montaj -->

      <fo:table border-collapse="collapse" width="100%" table-layout="fixed" margin-top="2mm">
	<fo:table-column column-number="1" border-style="solid" column-width="15%"/>
	<fo:table-column column-number="2" border-style="solid" column-width="35%"/>
	<fo:table-column column-number="3" border-style="solid" column-width="15%"/>
	<fo:table-column column-number="4" border-style="solid" column-width="35%"/>
	<fo:table-body>
	  <fo:table-row border-style="solid">
	    <fo:table-cell column-number="1"><fo:block>SETUM</fo:block></fo:table-cell>
	    <fo:table-cell column-number="2"><fo:block font-weight="bold">${doc.response.record["field[attribute::name='attribute1']"]}</fo:block></fo:table-cell>
	    <fo:table-cell column-number="3"><fo:block>Beneficiar</fo:block></fo:table-cell>
	    <fo:table-cell column-number="4"><fo:block></fo:block></fo:table-cell>
	  </fo:table-row>
	</fo:table-body>
      </fo:table>
      
      <fo:table space-before="5mm">
	<fo:table-column column-number="1" column-width="50%"/>
	<fo:table-column column-number="2" column-width="50%"/>
	<fo:table-body>
	  <fo:table-row>
	    <fo:table-cell column-number="1">
	      <fo:block font-weight="bold" text-align="right" margin-right="2.5cm">
		Total fara TVA: ${(cvnumber(doc["/response/record/field[attribute::name='totalFinal']"])/(cursul?number))?string("#,##0.00")} ${moneda}
	      </fo:block>
	      <fo:block font-weight="bold" text-align="right" margin-right="2.5cm">
		Total cu TVA: ${(cvnumber(doc["/response/record/field[attribute::name='totalFinalTva']"])/(cursul?number))?string("#,##0.00")} ${moneda}
	      </fo:block>

	    </fo:table-cell>
	    <fo:table-cell column-number="2">
	      <fo:table border-collapse="collapse" table-layout="fixed" width="100%" margin-bottom="1mm">
		<fo:table-column column-number="1" border-style="solid" column-width="25%"/>
		<fo:table-column column-number="2" border-style="solid" column-width="25%"/>
		<fo:table-column column-number="3" border-style="solid" column-width="25%"/>
		<fo:table-column column-number="4" border-style="solid" column-width="25%"/>
		
		<fo:table-body>
		  <fo:table-row border-style="solid">
		    <fo:table-cell column-number="1">
		      <fo:block>Tip</fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="2"><fo:block>Nr.</fo:block></fo:table-cell>
		    <fo:table-cell column-number="3">
		      <fo:block text-align="right">Suma [RON]</fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="4">
		      <fo:block text-align="right">Platit [RON]</fo:block>
		    </fo:table-cell>		 
		  </fo:table-row>
		  
		  <#list invoices as invoice>
		  <fo:table-row border-style="solid">
		    <fo:table-cell column-number="1">
		      <fo:block font-weight="bold">
			<!--${invoice["field[attribute::name='role']"]}-->
			F. ${search(doc["response/record/field[attribute::name='invoices']"], "role", invoice["field[attribute::name='role']"])}
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="2">
		      <fo:block font-weight="bold">${invoice["field[attribute::name='number']"]}</fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="3">
		      <fo:block text-align="right" font-weight="bold">${invoice["field[attribute::name='totalAmount']"]}</fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="4">
		      <fo:block text-align="right" font-weight="bold">${invoice["field[attribute::name='payedAmount']"]}</fo:block>
		    </fo:table-cell>
		  </fo:table-row>
		  <#list invoice["field[attribute::name='payments']/record"] as payment>
		  <fo:table-row border-style="solid">
		    <fo:table-cell column-number="1">
		      <fo:block>
			Plata
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="2" number-columns-spanned="2">
		      <fo:block font-weight="bold">${payment["field[attribute::name='number']"]}</fo:block>
		    </fo:table-cell>
		    <fo:table-cell column-number="4">
		      <fo:block text-align="right" font-weight="bold">${payment["field[attribute::name='amount']"]}</fo:block>
		    </fo:table-cell>
		  </fo:table-row>
		  </#list><!-- payment -->
		  </#list><!-- invoice -->
		  <fo:table-row border-style="solid">
		    <fo:table-cell column-number="1" number-columns-spanned="3">
		      <fo:block text-align="right" font-weight="bold">
			Rest de plata [${moneda}]: 
		      </fo:block>
		    </fo:table-cell>
		    <fo:table-cell>
		      <fo:block text-align="right" font-weight="bold">
			${doc["child::response/child::record/child::field[attribute::name='currencyDiferenta']"]}
		      </fo:block>
		    </fo:table-cell>
		  </fo:table-row>
		</fo:table-body>
	      </fo:table>
	      
	      
	    </fo:table-cell>
	  </fo:table-row>
	</fo:table-body>
      </fo:table>

      </fo:block> <!-- inchiderea blocului deschis la inceputul flow-lui -->


      <!-- Empty block, for page number citation -->
      <fo:block id="last-page-block"></fo:block>
    </fo:flow>
    <!-- /BODY -->
  </fo:page-sequence>
</fo:root>

<#-- search a value-list for a given value-id -->
<#function ssearch node vl_name>
 <#return search(node, vl_name, node.record["field[attribute::name='${vl_name}']"]) >
</#function>


<#-- search a value-list for a value coresponding to a value-id -->
<#function search node vl_name vl_key>


<#-- 
Searches the node for a child value-list with the given name, and in the
value list searches a child with the give key (value) and returns the associated label
-->


<#list node["value-list"] as vl>
	<#if vl["@name"] = vl_name>
		<#list vl["vl-item"] as vl_item>
			<#if vl_item.value = vl_key>
				<#return vl_item.label>
			</#if>
		</#list>
	</#if>
</#list>

<#return "N/A">

</#function>

<#-- afiseaza un sistem beneficiar -->
<#macro benefs name>
  <#local key_name = "benef${name?cap_first}" >
  <#local desc = usa.record["field[attribute::name='${key_name}']"]["@@text"] >
  <#local buc = usa.record["field[attribute::name='${key_name}Buc']"]["@@text"] >
  <#local tip = usa.record["field[attribute::name='benef${name?cap_first}Tip']"]["@@text"] >
  <!--
	sistem xml name: /${key_name}/
	sistem tip: /${tip}/
	sistem desc: /${desc}/
       	sistem buc: /${buc}/
  -->
  <#if buc?number &gt; 0 >
     <fo:table-row border-style="solid">
	<fo:table-cell column-number="1">
	  <fo:block>
	    <#nested/>
	  </fo:block>
	</fo:table-cell>
	<fo:table-cell column-number="2">
	  <fo:block font-weight="bold">
	    ${desc}
	    <#if tip?string != '' && tip?number != 0>${search(usa, "${key_name}Tip", tip)}</#if>
	  </fo:block>
	</fo:table-cell>
	<fo:table-cell column-number="3">
	  <fo:block font-weight="bold">
	    ${buc}
	  </fo:block>
	</fo:table-cell>
      </fo:table-row>
  </#if>
</#macro>

<#function cvnumber text_number>
<#-- make the conversion to number -->
     <#return text_number?replace(",", ".")?number />
</#function> 

<#-- 2 macro-uri cu care pui virgula in fata unui element dintr-o enumerare
     doar daca nu este primul element din acea enumerare
-->
<#macro "enum_init" sep=",">
  <#assign first_elem = true>
  <#assign enum_separator = sep>
</#macro>

<#macro "enum_next">
  <#if first_elem == true><#assign first_elem = false><#else>${enum_separator}</#if>
</#macro>

<#macro finisaj name>
<#if usa.record["field[attribute::name='${name}Id']"] != "0">
<fo:block border-bottom-style="solid">
  <fo:block><#nested/></fo:block>
  <fo:block font-weight="bold">${usa.record["field[attribute::name='${name}']"]}</fo:block>
</fo:block>
</#if>
</#macro>