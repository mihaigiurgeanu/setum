<?xml version="1.0" encoding="iso-8859-2"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <fo:layout-master-set>
    <fo:simple-page-master master-name="page"
			   page-height="297mm"
			   page-width="210mm"
			   margin-top="5mm"
			   margin-bottom="5mm"
			   margin-left="25mm"
			   margin-right="25mm">
      <fo:region-body margin-top="5mm" margin-bottom="5mm"
		      margin-left="0mm" margin-right="0mm"/>
      <fo:region-before extent="5mm"/>
      <fo:region-after extent="5mm"/>

    </fo:simple-page-master>
  </fo:layout-master-set>



  <fo:page-sequence master-reference="page">

    <!--HEADER-->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block></fo:block>
    </fo:static-content>
    <!--/HEADER-->


    <!--FOOTER-->
    <fo:static-content flow-name="xsl-region-after">
      <fo:block></fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">

      <fo:block font-size="10pt" font-family="Times">

      <!--
      <#assign lines=doc["response/record/field[attribute::name='lines']"] />
      <#assign usi=lines["record[field[attribute::name='product']/record/field[attribute::name='category.id']=9990]"] />
      <#assign nr_usi=usi?size />
      <#assign nr_crt_usa=0 />

      Nr linii: ${lines["record"]?size}
      Nr usi:   ${usi?size}

      -->

      <!-- date generale despre comanda (numar, data, nume client, etc.) -->
      <fo:table width="100%"  border-collapse="collapse" table-layout="fixed" space-after="1cm">
	<fo:table-column column-number="1" column-width="50%" border-style="none"/>
	<fo:table-column column-number="2" column-width="50%" border-style="none"/>

	<fo:table-body>
	  <fo:table-row>
	    <fo:table-cell column-number="1">
	      <fo:block font-weight="bold">
		Rest de plata = DE INCASAT: ${doc["/response/record/field[attribute::name='diferenta']"]} 
	      </fo:block>
	      <!--
		<fo:block>
		  C-da nr: ${doc["child::response/child::record/child::field[attribute::name='number']"]}
		  / Data: ${doc["child::response/child::record/child::field[attribute::name='date']"]}
		</fo:block>
	      -->
	    </fo:table-cell>
	    <fo:table-cell column-number="2">
	      <fo:block>
		Echipa de montaj:
	      </fo:block>
	    </fo:table-cell>
	  </fo:table-row>
	  
	</fo:table-body>
      </fo:table>
      <!-- end date generale despre oferta -->



      <fo:block text-align="center" font-weight="bold" space-after="5mm">
	PROCES VERBAL DE RECEPTIE MONTAJ
      </fo:block>
      <fo:block font-weight="bold">
	Prin prezentul proces verbal se certifica executia montajului conform instructiunilor de montaj, efectuarea probelor functionale pentru usa si incuietori, aspectul corespunzator al produsului:
      </fo:block>
      <fo:block space-before="5mm" space-after="5mm" font-weight="bold" text-align="right">
	Data montajului: ${doc["child::response/child::record/child::field[attribute::name='termenLivrare1']"]}
	Programare ora:  ${doc["child::response/child::record/child::field[attribute::name='deliveryHour']"]}
      </fo:block>
      
      <fo:block>Caracteristici usa metalica:</fo:block>
      <!-- vor apare doar produsele de tip usa metalica customizata -->      
      <#list usi as usa_line>
        <!--
        <#assign usa=usa_line["field[attribute::name='product']"] />
	<#assign nr_crt_usa=nr_crt_usa+1 />

	<#if nr_crt_usa<nr_usi><#assign pagebreak="always"/><#else><#assign pagebreak="auto"/></#if>
	Nr usa crt: ${nr_crt_usa}
	-->




	<!-- Start usa ${usa.record["field[attribute::name='code']"]} -->
	<fo:block>
	  ${nr_crt_usa})
	  ${usa.record["field[attribute::name='name']"]},

	  Varianta
	  <!--${usa.record["field[attribute::name='version']"]}-->
	  <!--${usa.record["field[attribute::name='k']"]} K-->
	  <!--${usa.record["field[attribute::name='subclass']"]}-->
        ${ssearch(usa, 'subclass')}
	  -
	  ${ssearch(usa, 'version')},
	  <!--izolata cu
	  ${ssearch(usa, 'isolation')}-->


	  <!-- start sisteme -->
	  <@enum_init /><!-- Initializeaza sistemul de despartire prin virgula -->
	  <#assign decupareSistemId = usa.record["field[attribute::name='decupareSistemId']"]>
	  <#if decupareSistemId != "0">
	  <@enum_next/> decupare
	  ${ssearch(usa, 'decupareSistemId')}
	  </#if>

	  <#assign broascaId = usa.record["field[attribute::name='broascaId']"]>
	  <#if broascaId != "0">
	  <@enum_next/> broasca
	  ${ssearch(usa, 'broascaId')}
	  ${usa.record["field[attribute::name='broascaBuc']"]} buc
	  </#if>

	  <#assign baraAntipanicaId = usa.record["field[attribute::name='baraAntipanicaId']"]>
	  <#if baraAntipanicaId != "0">
	  <@enum_next/> bara antipanica
	  ${ssearch(usa, 'baraAntipanicaId')}
	  ${usa.record["field[attribute::name='baraAntipanicaBuc']"]} buc
	  </#if>

	  <#assign sildId = usa.record["field[attribute::name='sildId']"]>
	  <#if sildId != "0">
	  <@enum_next/> sild
	  ${ssearch(usa, 'sildId')}
	  ${usa.record["field[attribute::name='sildBuc']"]} buc
	  </#if>
	  <#assign rozetaId = usa.record["field[attribute::name='rozetaId']"]>
	  <#if rozetaId != "0">
	  <@enum_next/> rozeta
	  ${ssearch(usa, 'rozetaId')}
	  ${usa.record["field[attribute::name='rozetaBuc']"]} buc
	  </#if>
	  <#assign manerId = usa.record["field[attribute::name='manerId']"]>
	  <#if manerId != "0">
	  <@enum_next/> maner
	  ${ssearch(usa, 'manerId')}
	  ${usa.record["field[attribute::name='manerBuc']"]} buc
	  </#if>

	   <#assign cilindruId = usa.record["field[attribute::name='cilindruId']"]>
	   <#if cilindruId != "0">
	   <@enum_next/> cilindru
	   ${ssearch(usa, 'cilindruId')}
	   ${usa.record["field[attribute::name='cilindruBuc']"]} buc

	   <#assign copiatCheieId = usa.record["field[attribute::name='copiatCheieId']"]>
	   <#if copiatCheieId != "0">
	   + ${usa.record["field[attribute::name='copiatCheieBuc']"]} chei (suplimentar)
	   </#if>
	   </#if>

	   <#assign yallaId = usa.record["field[attribute::name='yalla1Id']"]>
	   <#if yallaId != "0">
	   <@enum_next/> yalla
	   ${ssearch(usa, 'yalla1Id')}
	   ${usa.record["field[attribute::name='yalla1Buc']"]} buc

	   <#assign copiatCheieId = usa.record["field[attribute::name='copiatCheieId']"]>
	   <#if copiatCheieId != "0">
	   + ${usa.record["field[attribute::name='copiatCheieBuc']"]} chei (suplimentar)
	   </#if>
	   </#if>

	   <#assign yallaId = usa.record["field[attribute::name='yalla2Id']"]>
	   <#if yallaId != "0">
	   <@enum_next/> yalla
	   ${ssearch(usa, 'yalla2Id')}
	   ${usa.record["field[attribute::name='yalla2Buc']"]} buc

	   <#assign copiatCheieId = usa.record["field[attribute::name='copiatCheieId']"]>
	   <#if copiatCheieId != "0">
	   + ${usa.record["field[attribute::name='copiatCheieBuc']"]} chei (suplimentar)
	   </#if>
	   </#if>

	   <#assign amortizorId = usa.record["field[attribute::name='amortizorId']"]>
	   <#if amortizorId != "0">
	   <@enum_next/> amortizor
	   ${ssearch(usa, 'amortizorId')}
	   ${usa.record["field[attribute::name='amortizorBuc']"]} buc
	   </#if>

	   <#assign alteSisteme1Id = usa.record["field[attribute::name='alteSisteme1Id']"]>
	   <#if alteSisteme1Id != "0">
	   <@enum_next/> ${ssearch(usa, 'alteSisteme1Id')}
	   ${usa.record["field[attribute::name='alteSisteme1Buc']"]} buc
	   </#if>

	   <#assign alteSisteme2Id = usa.record["field[attribute::name='alteSisteme2Id']"]>
	   <#if alteSisteme2Id != "0">
	   <@enum_next/> ${ssearch(usa, 'alteSisteme2Id')}
	   ${usa.record["field[attribute::name='alteSisteme2Buc']"]} buc,
	   </#if>

	   <#assign vizorId = usa.record["field[attribute::name='vizorId']"]>
	   <#if vizorId != "0">
	   <@enum_next/> vizor
	   ${ssearch(usa, 'vizorId')}
	   ${usa.record["field[attribute::name='vizorBuc']"]} buc
	   </#if>

	   <@benefs name="broasca">broasca</@benefs>
	   <@benefs name="cilindru">cilindru</@benefs>
	   <@benefs name="sild">sild</@benefs>
	   <@benefs name="yalla">yalla</@benefs>
	   <@benefs name="baraAntipanica">bara antipanica</@benefs>
	   <@benefs name="maner">maner</@benefs>
	   <@benefs name="selectorOrdine">selector ordine</@benefs>
	   <@benefs name="amortizor">amortizor</@benefs>
	   <@benefs name="alteSisteme1"></@benefs>
	   <@benefs name="alteSisteme2"></@benefs>
	   <!-- end sisteme -->
	   <@enum_next/>
	   <!-- start optiuni -->
	   <#assign optiuni=usa.record["field[attribute::name='parts']/record/field[attribute::name='part']"]><#t>
	   <#list optiuni as optiune>
	   <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/panoulateral">
	   Panou lateral ${optiune.record["field[attribute::name='lpl']"]}x${optiune.record["field[attribute::name='hpl']"]},
	   </#if>
	   <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/supralumina">
	   Supralumina ${optiune.record["field[attribute::name='ls']"]}x${optiune.record["field[attribute::name='hs']"]},
	   </#if>
	   <#if optiune.record["field[attribute::name='businessCategory']"] = "http://www.kds.ro/erp/businessCategory/setum/optiuni/fereastra">
	   Fereastra ${optiune.record["field[attribute::name='lf']"]}x${optiune.record["field[attribute::name='hf']"]},
	   </#if>
	   </#list>
	   <!-- end optiuni -->

	   <!-- start finisaje -->
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
	   <!-- finisaje egale, listez doar unul -->
	   Finisaj ${usa.record["field[attribute::name='intFinisajBlat']"]}
	   <#else>
	   <!-- finisaje neegale ... le listez pe fiecare -->
	   Finisaj
	   <#if usa.record["field[attribute::name='intFinisajBlatId']"] != "0">
	   Blat (interior)
	   ${usa.record["field[attribute::name='intFinisajBlat']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajBlatId']"] != "0">
	   Blat (exterior)
	   ${usa.record["field[attribute::name='extFinisajBlat']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='intFinisajTocId']"] != "0">
	   Toc (interior)
	   ${usa.record["field[attribute::name='intFinisajToc']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajTocId']"] != "0">
	   Toc (exterior)
	   ${usa.record["field[attribute::name='extFinisajToc']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='intFinisajGrilajId']"] != "0">
	   Grilaj (interior)
	   ${usa.record["field[attribute::name='intFinisajGrilaj']"]}
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajGrilajId']"] != "0">
	   Grilaj (exterior)
	   ${usa.record["field[attribute::name='extFinisajGrilaj']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='intFinisajFereastraId']"] != "0">
	   Fereastra (interior)
	   ${usa.record["field[attribute::name='intFinisajFereastra']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajFereastraId']"] != "0">
	   Fereastra (exterior)
	   ${usa.record["field[attribute::name='extFinisajFereastra']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='intFinisajSupraluminaId']"] != "0">
	   Supralumina (interior)
	   ${usa.record["field[attribute::name='intFinisajSupralumina']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajSupraluminaId']"] != "0">
	   Supralumina (exterior)
	   ${usa.record["field[attribute::name='extFinisajSupralumina']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='intFinisajPanouLateralId']"] != "0">
	   Panou lateral (interior)
	   ${usa.record["field[attribute::name='intFinisajPanouLateral']"]}/
	   </#if>
	   <#if usa.record["field[attribute::name='extFinisajPanouLateralId']"] != "0">
	   Panou lateral (exterior)
	   ${usa.record["field[attribute::name='extFinisajPanouLateral']"]}
	   </#if>

	   </#if>
	   <!-- end finisaje -->
	   

	
	 </fo:block>
	 <fo:block font-weight="bold">
	   Dimensiuni: ${usa.record["field[attribute::name='lexec']"]}x${usa.record["field[attribute::name='hexec']"]} =
	   ${usa_line["field[attribute::name='quantity']"]} buc., Deschidere: ${ssearch(usa, 'openingSide')} ${ssearch(usa, 'openingDir')}
	 </fo:block>
	 <!-- sfarsit linie de comanda -->
	 <!-- End usa ${usa.record["field[attribute::name='code']"]} -->
	 </#list>
      
	 <fo:block text-align="right" font-weight="bold" space-before="5mm" space-after="5mm">
	   Semnatura beneficiar: ..........................................
	 </fo:block>

	 <fo:block font-weight="bold">
	   La adresa se demonteaza: ${ssearch(doc.response, 'tipDemontare')}
	 </fo:block>

	 <fo:block>
	   <!-- start tip montaj -->
	   Tip montaj: 
	   <#list usi as usa_line>
	   <#assign usa=usa_line["field[attribute::name='product']"] />
	   ${usa.record["field[attribute::name='name']"]}/${search(lines, "montajId", usa_line["field[attribute::name='montajId']"])}
	   ${usa_line["field[attribute::name='quantity']"]} buc.;
	   </#list>
	   <!-- end tip montaj -->
	 </fo:block>
	 <fo:block space-after="1cm" linefeed-treatment="preserve">
	   Observatii montaj: ${doc.response.record["field[attribute::name='observatii']"]}
	 </fo:block>

	 <fo:block font-weight="bold">
	   Beneficiar ${doc["child::response/child::record/child::field[attribute::name='clientName']"]}
	   C-da/Contr nr. ${doc["child::response/child::record/child::field[attribute::name='number']"]}
	   /data ${doc["child::response/child::record/child::field[attribute::name='date']"]}
	 </fo:block>

	 <fo:block>Adresa montaj:</fo:block>
	 <fo:block space-after="1cm">
	   <fo:block>${doc["child::response/child::record/child::field[attribute::name='adresaMontaj']"]}</fo:block>
	   <fo:block>Reper: ${doc["child::response/child::record/child::field[attribute::name='adresaReper']"]}</fo:block>
	   <fo:block>Telefon: ${doc["child::response/child::record/child::field[attribute::name='telefon']"]}</fo:block>
	   <fo:block>Persoana de contact: ${doc["child::response/child::record/child::field[attribute::name='contact']"]}</fo:block>

	 </fo:block>
	 
	 <!-- end proces verbal -->


	 <!-- start fisa RLV -->

	 <!-- start header rlv -->
	 <fo:table width="100%"  border-collapse="collapse" table-layout="fixed" space-after="1cm">
	   <fo:table-column column-number="1" column-width="50%" border-style="none"/>
	   <fo:table-column column-number="2" column-width="50%" border-style="none"/>

	   <fo:table-body>
	     <fo:table-row>
	       <fo:table-cell column-number="1">
		 <fo:block font-weight="bold">
		   FISA RLV - usa metalica:
		 </fo:block>
	       </fo:table-cell>
	       <fo:table-cell column-number="2">
		 <fo:block font-weight="bold">
		   Data RLV: .............../ora.........
		 </fo:block>
		 <fo:block font-weight="bold">
		   Persoana RLV: ........................
		 </fo:block>
	       </fo:table-cell>
	     </fo:table-row>
	     
	   </fo:table-body>
	 </fo:table>
	 <!-- end header rlv -->


	 <fo:table width="100%"  border-collapse="collapse" table-layout="fixed" space-after="1cm">
	   <fo:table-column column-number="1" column-width="50%" border-style="none"/>
	   <fo:table-column column-number="2" column-width="50%" border-style="none"/>

	   <fo:table-body>


	     <#assign nr_crt_usa=0 />
	     <#list usi as usa_line>
	     <!--
		 <#assign usa=usa_line["field[attribute::name='product']"] />
		 <#assign nr_crt_usa=nr_crt_usa+1 />
		 
		 <#if nr_crt_usa<nr_usi><#assign pagebreak="always"/><#else><#assign pagebreak="auto"/></#if>
		 Nr usa crt: ${nr_crt_usa}
	     -->
	     
	     <!-- Start usa ${usa.record["field[attribute::name='code']"]} -->
	     <fo:table-row>
	       <fo:table-cell>
		 <fo:block font-weight="bold">
		   ${nr_crt_usa})
		   ${usa.record["field[attribute::name='name']"]},
		   Varianta
		   <!--${usa.record["field[attribute::name='version']"]}-->
		   <!--${usa.record["field[attribute::name='k']"]} K-->
		   <!--${usa.record["field[attribute::name='subclass']"]}-->
               ${ssearch(usa, 'subclass')}
		   -
		   ${ssearch(usa, 'version')}
		   <!--izolata cu
		   ${ssearch(usa, 'isolation')}-->
		 </fo:block>
		 <fo:block font-weight="bold">
		   Dimensiuni comanda:
		 </fo:block>
		 <fo:block>
		   ${usa.record["field[attribute::name='lexec']"]}x${usa.record["field[attribute::name='hexec']"]} =
		   ${usa_line["field[attribute::name='quantity']"]} buc.
		 </fo:block>
	       </fo:table-cell>
	       <fo:table-cell>
		 <fo:block font-weight="bold">
		   Dimensiuni masurate:
		 </fo:block>
		 <fo:block font-weight="bold">
		   ...................................................................
		 </fo:block>
		 <fo:block font-weight="bold">
		   Obs.:..............................................................
		 </fo:block>
	       </fo:table-cell>
	     </fo:table-row>
	     <!-- end usa ${usa.record["field[attribute::name='code']"]} -->
	     </#list>
	   </fo:table-body>
	 </fo:table>

	 <fo:block font-weight="bold">
	   Beneficiar ${doc["child::response/child::record/child::field[attribute::name='clientName']"]}
	   C-da/Contr nr. ${doc["child::response/child::record/child::field[attribute::name='number']"]}
	   /data ${doc["child::response/child::record/child::field[attribute::name='date']"]}
	 </fo:block>

	 <fo:block>Adresa montaj:</fo:block>
	 <fo:block space-after="1cm">
	   <fo:block>${doc["child::response/child::record/child::field[attribute::name='adresaMontaj']"]}</fo:block>
	   <fo:block>Reper: ${doc["child::response/child::record/child::field[attribute::name='adresaReper']"]}</fo:block>
	   <fo:block>Telefon: ${doc["child::response/child::record/child::field[attribute::name='telefon']"]}</fo:block>
	   <fo:block>Persoana de contact: ${doc["child::response/child::record/child::field[attribute::name='contact']"]}</fo:block>
	 </fo:block>
	 <!-- end fisa RLV -->
	       
       </fo:block> <!-- inchiderea blocului deschis la inceputul flow-lui -->
       
       <!-- Empty block, for page number citation -->
       <fo:block id="last-page-block"></fo:block>
     </fo:flow>
     <!-- /BODY -->
   </fo:page-sequence>

   <!-- search a value-list for a given value-id -->
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

<#-- 2 macro-uri cu care pui virgula in fata unui element dintr-o enumerare
     doar daca nu este primul element din acea enumerare
-->
<#macro "enum_init">
  <#assign first_elem = true>
</#macro>

<#macro "enum_next">
  <#if first_elem == true><#assign first_elem = false><#else>,</#if>
</#macro>

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
     <@enum_next />
     <#nested>
     ${desc}
     <#if tip?string != '' && tip?number != 0>${search(usa, "${key_name}Tip", tip)}</#if>
     <#if buc?number != 1>${buc} buc</#if>
  </#if>
</#macro>

</fo:root>
