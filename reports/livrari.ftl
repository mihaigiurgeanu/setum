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
		<fo:block text-align="center">Tel.:  021.316.39.57, 021.316.05.78 </fo:block>
            <fo:block text-align="center">Tel/Fax: 021.316.05.90</fo:block>
                <!--
		<fo:block text-align="center">Secretariat: Tel/Fax: 316.05.88; Tel: 316.39.57</fo:block>
		<fo:block text-align="center">Centrala: Tel: 316.05.78 Dep. Economic: Tel: 317.25.40</fo:block>
                -->
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
      <fo:block text-align="end" font-size="8pt"
		border-after-style="solid">Pagina <fo:page-number/></fo:block>
      <fo:block text-align="center" font-size="6pt">Nr. Reg.Com. J40/314/1991   CONT: RO94RNCB0077008452610001 B.C.R. Suc. Sector 6 
         C.F.:RO 458556; Capital Social: 1.415.115 LEI</fo:block>
    </fo:static-content>
    <!--/FOOTER-->


    <!-- BODY -->
    <fo:flow flow-name="xsl-region-body">

      <fo:block text-align="center" margin-top="10mm" margin-bottom="10mm">
	Lista programare livrari usi metalice
	<fo:block text-align="center">
	  ${doc.response.record["field[attribute::name='livrariRStart']"]}
	  -
	  ${doc.response.record["field[attribute::name='livrariREnd']"]}
	</fo:block>
      </fo:block>



      <fo:block font-size="10pt" font-family="Times"> <!-- Begin of Report Body -->



	<#--
	Structura date:
	/response/record/fied[attribute::name="livrari"] - lista de comenzi
	/response/return/attribute::code - codul de raspuns al raportului (0 = succes)
	
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record - datele unei comenzi
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="termenLivrare1"] - data montaj
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="clientName"] - beneficiar
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="deliveryHour"] - ora montajului
	
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"] - linii de comanda
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record - datele liniei de comanda
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="quantity"] - nr buc
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]//record/field[attribute::name="lines"]record/field[attribute::name="otherLocation"] - locatia
	
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="product"]/record - date despre produs
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="product"]/record/field[attribute::name="category.id"]=9990 - conditie de selectie a unei usi metalice
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="product"]/record/field[attribute::name="name"] - codul beneficiar al usii
	
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="product"]/record/field[attribute::name="lexec"] - dimensiune
	/response/record/fied[attribute::name="livrari"]/record/field[attribute::name="order"]/record/field[attribute::name="lines"]/record/field[attribute::name="product"]/record/field[attribute::name="hexec"] - dimensiune
	
	-->


	<#-- selectez produsele de tip usa, si pornind de la produs merg in sus sa selectez
	     datele din linia de comanda si din comanda
	-->
	
	<fo:table border-collapse="collapse" table-layout="fixed" width="100%">
	  <fo:table-column column-number="1" column-width="19mm" border-style="solid"/><!-- Data -->
	  <fo:table-column column-number="2" column-width="30mm" border-style="solid"/><!-- Beneficiar -->
	  <fo:table-column column-number="3" column-width="41mm" border-style="solid"/><!-- Tip -->
	  <fo:table-column column-number="4" column-width="20mm" border-style="solid"/><!-- Dimensiuni -->
	  <fo:table-column column-number="5" column-width="10mm" border-style="solid"/><!-- Buc -->
	  <fo:table-column column-number="6" column-width="22mm" border-style="solid"/><!-- Locatie -->
	  <fo:table-column column-number="7" column-width="9mm" border-style="solid"/><!-- Ora -->
	  <fo:table-column column-number="8" column-width="13mm" border-style="solid"/><!-- Montaj -->

	  <fo:table-body>
	    <fo:table-row border-style="solid">
	      <fo:table-cell column-number="1" text-align="center"><fo:block>Data</fo:block></fo:table-cell>
	      <fo:table-cell column-number="2" text-align="center"><fo:block>Beneficiar</fo:block></fo:table-cell>
	      <fo:table-cell column-number="3" text-align="center"><fo:block>Tip</fo:block></fo:table-cell>
	      <fo:table-cell column-number="4" text-align="center"><fo:block>Dimensiuni (mm)</fo:block></fo:table-cell>
	      <fo:table-cell column-number="5" text-align="center"><fo:block>BUC</fo:block></fo:table-cell>
	      <fo:table-cell column-number="6" text-align="center"><fo:block>Locatie</fo:block></fo:table-cell>
	      <fo:table-cell column-number="7" text-align="center"><fo:block>Ora</fo:block></fo:table-cell>
	      <fo:table-cell column-number="8" text-align="center"><fo:block>Montaj</fo:block></fo:table-cell>
	    </fo:table-row>

	    <#list doc["/response/record/field[attribute::name=\"livrari\"]/record/field[attribute::name=\"order\"]/record/field[attribute::name=\"lines\"]/record/field[attribute::name=\"product\"]/record[field[attribute::name=\"category.id\"]=9990]"] as usa>
	    <!-- Begin usa -->

	    <#assign order=usa["ancestor::field[attribute::name='order']/record"]>
	    <#assign linie=usa["ancestor::record[parent::field[attribute::name='lines']]"]>
	    <#assign locatie=linie["field[attribute::name='otherLocation']"]>
	    <#if locatie = "null"><#assign locatie="-"></#if>

	    <fo:table-row border-style="solid">	      
	      <fo:table-cell column-number="1" text-align="center">
		<fo:block margin="1mm">
		  ${order["field[attribute::name='termenLivrare1']"]}
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2">
		<fo:block margin="1mm">
		  ${order["field[attribute::name='clientName']"]}
		</fo:block>
		<fo:block margin="1mm" space-before="1mm">
		  Comanda: ${order["field[attribute::name='number']"]} din ${order["field[attribute::name='date']"]}
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="3">
		<fo:block margin="1mm">
		  <!-- ${usa["field[attribute::name=\"name\"]"]} -->
		  ${ssearch(usa["parent::*"], "version")}
		</fo:block>
		<fo:block margin="1mm">
		  <!-- finisajele -->
		  <#if (usa["field[attribute::name='intFinisajTocId']"] = "0" ||
			        usa["field[attribute::name='finisajTocBlat']"] = "true") &&
			       (usa["field[attribute::name='intFinisajGrilajId']"] = "0" ||
			        usa["field[attribute::name='finisajGrilajBlat']"] = "true") &&
			       (usa["field[attribute::name='intFinisajFereastraId']"] = "0" ||
			        usa["field[attribute::name='finisajFereastraBlat']"] = "true") &&
			       (usa["field[attribute::name='intFinisajSupraluminaId']"] = "0" ||
			        usa["field[attribute::name='finisajSupraluminaBlat']"] = "true") &&
			       (usa["field[attribute::name='intFinisajPanouLateralId']"] = "0" ||
			        usa["field[attribute::name='finisajPanouLateralBlat']"] = "true") &&
			       (usa["field[attribute::name='extFinisajBlatId']"] = "0" ||
			        usa["field[attribute::name='finisajBlatExtInt']"] = "true") &&
			       (usa["field[attribute::name='extFinisajTocId']"] = "0" ||
			        usa["field[attribute::name='finisajTocExtInt']"] = "true") &&
			       (usa["field[attribute::name='extFinisajGrilajId']"] = "0" ||
			        usa["field[attribute::name='finisajGrilajExtInt']"] = "true") &&
			       (usa["field[attribute::name='extFinisajFereastraId']"] = "0" ||
			        usa["field[attribute::name='finisajFereastraExtInt']"] = "true") &&
			       (usa["field[attribute::name='extFinisajSupraluminaId']"] = "0" ||
			        usa["field[attribute::name='finisajSupraluminaExtInt']"] = "true") &&
			       (usa["field[attribute::name='extFinisajPanouLateralId']"] = "0" ||
			        usa["field[attribute::name='finisajPanouLateralExtInt']"] = "true")>
			${usa["field[attribute::name='intFinisajBlat']"]}
		<#else>
		            <#if usa["field[attribute::name='intFinisajBlatId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Blat (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajBlat']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='extFinisajBlatId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Blat (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajBlat']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='intFinisajTocId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Toc (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajToc']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='extFinisajTocId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Toc (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajToc']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='intFinisajGrilajId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Grilaj (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajGrilaj']"]}</fo:block>
			    </fo:block>
			    </#if>
			     <#if usa["field[attribute::name='extFinisajGrilajId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Grilaj (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajGrilaj']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='intFinisajFereastraId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Fereastra (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajFereastra']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='extFinisajFereastraId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Fereastra (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajFereastra']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='intFinisajSupraluminaId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Supralumina (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajSupralumina']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='extFinisajSupraluminaId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Supralumina (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajSupralumina']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='intFinisajPanouLateralId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Panou lateral (interior)</fo:block>
			      <fo:block>${usa["field[attribute::name='intFinisajPanouLateral']"]}</fo:block>
			    </fo:block>
			    </#if>
			    <#if usa["field[attribute::name='extFinisajPanouLateralId']"] != "0">
			    <fo:block border-bottom-style="solid">
			      <fo:block>Panou lateral (exterior)</fo:block>
			      <fo:block>${usa["field[attribute::name='extFinisajPanouLateral']"]}</fo:block>
			    </fo:block>
			    </#if>
		</#if>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="4">
		<fo:block margin="1mm">
		   ${usa["field[attribute::name=\"lexec\"]"]} x ${usa["field[attribute::name=\"hexec\"]"]}
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="5" text-align="right">
		<fo:block margin="1mm">
		  ${linie["field[attribute::name='quantity']"]}
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="6"><fo:block margin="1mm">${locatie}</fo:block></fo:table-cell>
	      <fo:table-cell column-number="7" text-align="center">
		<fo:block margin="1mm">${order["field[attribute::name='deliveryHour']"]}</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="8" text-align="center">
		<fo:block margin="1mm">${search(linie["parent::*"], "montajId", linie["field[attribute::name='montajId']"])}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>
	    <!-- end usa -->
	    </#list>
	  </fo:table-body>
	</fo:table>
	

      </fo:block> <!-- End of Report Body -->


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


</fo:root>
