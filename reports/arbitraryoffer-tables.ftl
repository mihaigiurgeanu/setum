<?xml version="1.0" encoding="utf-8"?>

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

    <fo:static-content flow-name="xsl-region-before">
      <fo:block border-before-style="solid"
		border-after-style="solid">
	<fo:table table-layout="auto">
	  <fo:table-column column-number="1" column-width="50%"/>
	  <fo:table-column column-number="2" column-width="50%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell>
		<fo:block>
		  <fo:external-graphic src="images/sigla.png" content-height="20mm"/>
		</fo:block>
	      </fo:table-cell>
	      <fo:table-cell font-size="6pt" display-align="center">
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
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="6pt"
		border-before-style="solid">Pagina <fo:page-number/></fo:block>
    </fo:static-content>






    <fo:flow flow-name="xsl-region-body">

      <fo:block>
	<fo:table width="90%">
	  <fo:table-column column-number="1" column-width="30%"/>
	  <fo:table-column column-number="2" column-width="70%"/>

	  <fo:table-body>
	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Oferta Nr:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='no']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Catre:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='clientName']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Din data:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='dateFrom']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Valabilitate pana:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='dateTo']"]} (an-luna-zi)</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	    <fo:table-row>
	      <fo:table-cell column-number="1" border-style="solid" font-weight="bold">
		<fo:block>Observatii:</fo:block>
	      </fo:table-cell>
	      <fo:table-cell column-number="2" border-style="solid">
		<fo:block>${doc["child::response/child::record/child::field[attribute::name='description']"]}</fo:block>
	      </fo:table-cell>
	    </fo:table-row>

	  </fo:table-body>
	</fo:table>
      </fo:block>



    <fo:table space-before="2em" space-after="2em">
      <fo:table-column column-number="1" column-width="30%"/> <!-- produs -->
      <fo:table-column column-number="2" column-width="50%"/> <!-- cod -->
      <fo:table-column column-number="3" column-width="20%"/> <!-- valoare -->

      <fo:table-body>
	<fo:table-row>
	  <fo:table-cell column-number="1" border-before-style="solid" border-after-style="solid"><fo:block>Denumire</fo:block></fo:table-cell>
	  <fo:table-cell column-number="2" border-before-style="solid" border-after-style="solid"><fo:block>Cod</fo:block></fo:table-cell>
	  <fo:table-cell column-number="3" border-before-style="solid" border-after-style="solid"><fo:block text-align="end">Pret</fo:block></fo:table-cell>		    
	</fo:table-row>


	<#assign lineno=1>
	<#list doc["child::response/child::record/child::field[attribute::name='lines']/child::record"] as record>

	<fo:table-row>
	  <fo:table-cell column-number="1"><fo:block>${lineno}. ${record["child::field[attribute::name='productCategory']"]}</fo:block></fo:table-cell>
	  <fo:table-cell column-number="2"><fo:block>${record["child::field[attribute::name='productName']"]}</fo:block></fo:table-cell>
	  <fo:table-cell column-number="3" text-align="end"><fo:block>${record["child::field[attribute::name='vatPrice']"]}</fo:block></fo:table-cell>		    
	</fo:table-row>

	<#assign lineno = lineno + 1>
	</#list>

      </fo:table-body>
    </fo:table>



    <#list doc["child::response/child::record/child::field[attribute::name='lines']/child::record"] as record>
      <#switch record["child::field[attribute::name='product']/child::record/child::field[attribute::name='category.id']"]?number>
      <#case 9990>
      <#case 9993>
      <fo:block border-before-style="solid">
      <#visit record["child::field[attribute::name='product']/child::record"]>
      </fo:block>
      </#switch>
    </#list>


    </fo:flow>









  </fo:page-sequence>
  
</fo:root>




<#macro "record">
	<#switch .node["child::field[attribute::name='category.id']/child::text()"]?number>
	<#case 9990>
		<!-- Usa metalica customizata -->
		<fo:block space-after="2em">
		<fo:block>
		Usa metalica ${.node["child::field[attribute::name='name']"]}:
		</fo:block>
		<fo:table>

		<fo:table-column column-number="1" column-width="25%"/>
		<fo:table-column column-number="2" column-width="25%"/>
		<fo:table-column column-number="3" column-width="25%"/>
		<fo:table-column column-number="4" column-width="25%"/>

		<fo:table-body>

		<fo:table-row>

		<fo:table-cell column-number="1"><fo:block>Material: ${search(.node?parent, "material", .node["child::field[attribute::name='material']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>Nr canate:${.node["child::field[attribute::name='k']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>Lg = ${.node["child::field[attribute::name='lg']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="4"><fo:block>Hg = ${.node["child::field[attribute::name='hg']"]}</fo:block></fo:table-cell>

		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block>Le = ${.node["child::field[attribute::name='le']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>He = ${.node["child::field[attribute::name='he']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>L curent = ${.node["child::field[attribute::name='lCurrent']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block>L util = ${.node["child::field[attribute::name='lUtil']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>H util = ${.node["child::field[attribute::name='hUtil']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>L foaie = ${.node["child::field[attribute::name='lFoaie']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="4"><fo:block>H foaie = ${.node["child::field[attribute::name='hFoaie']"]}</fo:block></fo:table-cell>

		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block>Foaie interior: ${search(.node?parent, "intFoil", .node["child::field[attribute::name='intFoil']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>Foaie exterior: ${search(.node?parent, "extFoil", .node["child::field[attribute::name='extFoil']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>Izolatie: ${search(.node?parent, "isolation", .node["child::field[attribute::name='isolation']"])?cap_first}</fo:block></fo:table-cell>

		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block></fo:block>Deschidere spre: ${search(.node?parent, "openingDir", .node["child::field[attribute::name='openingDir']"])?cap_first}</fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>Deschiderea in: ${search(.node?parent, "openingSide", .node["child::field[attribute::name='openingSide']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>Pozitionare foaie: ${search(.node?parent, "foilPosition", .node["child::field[attribute::name='foilPosition']"])?cap_first}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block>Tip toc: ${search(.node?parent, "frameType", .node["child::field[attribute::name='frameType']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>L toc = ${.node["child::field[attribute::name='lFrame']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>b toc = ${.node["child::field[attribute::name='bFrame']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="4"><fo:block>c toc = ${.node["child::field[attribute::name='cFrame']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell column-number="1"><fo:block>Tip prag: ${search(.node?parent, "tresholdType", .node["child::field[attribute::name='tresholdType']"])?cap_first}</fo:block></fo:table-cell>
		<fo:table-cell column-number="2"><fo:block>L prag = ${.node["child::field[attribute::name='lTreshold']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="3"><fo:block>H prag = ${.node["child::field[attribute::name='hTreshold']"]}</fo:block></fo:table-cell>
		<fo:table-cell column-number="4"><fo:block>C prag = ${.node["child::field[attribute::name='cTreshold']"]}</fo:block></fo:table-cell>

		</fo:table-row>

		</fo:table-body>
		</fo:table>

		<fo:table space-before="2em">
		<fo:table-column column-number="1" column-width="30%"/><!-- Sistem -->
		<fo:table-column column-number="2" column-width="50%"/><!-- Cod -->
		<fo:table-column column-number="3" column-width="20%"/><!-- Cantitate -->

		<fo:table-header space-after="1.5em">
		<fo:table-row>
		<fo:table-cell><fo:block font-weight="bold">Sistem</fo:block></fo:table-cell>
		<fo:table-cell><fo:block font-weight="bold">Cod</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block font-weight="bold">Cantitate</fo:block></fo:table-cell>
		</fo:table-row>
		</fo:table-header>

		<fo:table-body>

		<fo:table-row>
		<fo:table-cell><fo:block>Broasca</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>${search(.node?parent,"broascaId",.node["child::field[attribute::name='broascaId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='broascaBuc']"]}</fo:block></fo:table-cell>		
		</fo:table-row>
		
		<fo:table-row>
		<fo:table-cell><fo:block>Cilindru</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>${search(.node?parent,"cilindruId",.node["child::field[attribute::name='cilindruId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='cilindruBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Copiat chei</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>${search(.node?parent,"copiatCheieId",.node["child::field[attribute::name='copiatCheieId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='copiatCheieBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Vizor</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>${search(.node?parent,"vizorId",.node["child::field[attribute::name='vizorId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='vizorBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Sild</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>
			${search(.node?parent,"sildId",.node["child::field[attribute::name='sildId']"])}
			${.node["child::field[attribute::name='sildTip']"]}
			${.node["child::field[attribute::name='sildCuloare']"]}
		</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='sildBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Rozeta</fo:block></fo:table-cell>		
		<fo:table-cell><fo:block>
			${search(.node?parent,"rozetaId",.node["child::field[attribute::name='rozetaId']"])}
			${.node["child::field[attribute::name='rozetaTip']"]}
			${.node["child::field[attribute::name='rozetaCuloare']"]}
		</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='rozetaBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Maner</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>
			${search(.node?parent,"manerId",.node["child::field[attribute::name='manerId']"])}
			${.node["child::field[attribute::name='manerTip']"]}
			${.node["child::field[attribute::name='manerCuloare']"]}
		</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='manerBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Yalla</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"yalla1Id",.node["child::field[attribute::name='yalla1Id']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='yalla1Buc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Yalla</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"yalla2Id",.node["child::field[attribute::name='yalla2Id']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='yalla2Buc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Bara antipanica</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"baraAntipanicaId",.node["child::field[attribute::name='baraAntipanicaId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='baraAntipanicaBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Yalla</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"yalla2Id",.node["child::field[attribute::name='yalla2Id']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='yalla2Buc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Maner semicilindru</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"manerSemicilindruId",.node["child::field[attribute::name='manerSemicilindruId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='manerSemicilindruBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Selector ordine</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"selectorOrdineId",.node["child::field[attribute::name='selectorOrdineId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='selectorOrdineBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Amortizor</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"amortizorId",.node["child::field[attribute::name='amortizorId']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='amortizorBuc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Alt sistem</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"alteSisteme1Id",.node["child::field[attribute::name='alteSisteme1Id']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='alteSisteme1Buc']"]}</fo:block></fo:table-cell>
		</fo:table-row>

		<fo:table-row>
		<fo:table-cell><fo:block>Alt sistem</fo:block></fo:table-cell>
		<fo:table-cell><fo:block>${search(.node?parent,"alteSisteme2Id",.node["child::field[attribute::name='alteSisteme2Id']"])}</fo:block></fo:table-cell>
		<fo:table-cell text-align="right"><fo:block>${.node["child::field[attribute::name='alteSisteme2Buc']"]}</fo:block></fo:table-cell>
		</fo:table-row>



		</fo:table-body>
		</fo:table>




		</fo:block>
		<#list .node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"] as record>
		<#visit record>
		</#list>

		<#break>

	<#case 9980>
	       <fo:block space-after="2em" text-align="justify">
	       Panou lateral ${.node["child::field[attribute::name='lpl']"]} mm X ${.node["child::field[attribute::name='hpl']"]} mm
	       avand ${.node["child::field[attribute::name='cells']"]} celule
	       <fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${.node["child::field[attribute::name='quantity']"]} buc.
	       </fo:block>
	       <#break>

	<#case 9981>
	       <fo:block space-after="2em" text-align="justify">
	       Supralumina ${.node["child::field[attribute::name='ls']"]} mm X ${.node["child::field[attribute::name='hs']"]} mm
	       avand ${.node["child::field[attribute::name='cells']"]} celule
	       <fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${.node["child::field[attribute::name='quantity']"]} buc.
	       </fo:block>
	       <#break>

	<#case 9982>
	       <fo:block space-after="2em" text-align="justify">
	       Gauri aerisire cu diametrul ${.node["child::field[attribute::name='diametru']"]} mm,
	       pasul ${.node["child::field[attribute::name='pas']"]} mm
	       <fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${.node["child::field[attribute::name='nrRanduri']"]} randuri.
	       </fo:block>
	       <#break>

	<#case 9983>
	       <!-- Grila de ventilatie -->
	       <fo:block space-after="2em" text-align="justify">
	       Grila ventilatie ${.node["child::field[attribute::name='lgv']"]} mm X ${.node["child::field[attribute::name='hgv']"]} mm
	       <fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${.node["child::field[attribute::name='quantity']"]} buc.
	       </fo:block>
	       <#break>
	<#case 9989>
	       <!-- Fereastra -->
	       <fo:block space-after="2em" text-align="justify">
	       Fereastra ${.node["child::field[attribute::name='lf']"]} mm X ${.node["child::field[attribute::name='hf']"]} mm
	       <fo:leader leader-pattern="dots" leader-length.optimum="100%"/>${.node["child::field[attribute::name='quantity']"]} buc.
	       </fo:block>
	       <#break>
	<#default>
		<fo:block space-after="2em">		
		${.node["child::field[attribute::name='category.name']"]?cap_first}
		${.node["child::field[attribute::name='code']"]}:
		<@partslisting .node/>
		</fo:block>
	</#switch>
</#macro>




<#macro "partslisting" node>

<#local records = node["child::field[attribute::name='parts']/child::record/child::field[attribute::name='part']/child::record"]>

<#if records?size &gt; 0>

<fo:table>

<fo:table-column column-number="1" column-width="30%"/> <!-- produs -->
<fo:table-column column-number="2" column-width="70%"/> <!-- cod -->

<fo:table-body>

<#local lineno=1>
<#list records as record>

<fo:table-row>
  <fo:table-cell column-number="1"><fo:block>${lineno}. ${record["child::field[attribute::name='category.name']"]}</fo:block></fo:table-cell>
  <fo:table-cell column-number="2"><fo:block>${record["child::field[attribute::name='code']"]}</fo:block></fo:table-cell>
</fo:table-row>

<#local lineno= lineno + 1>
</#list>


</fo:table-body>
</fo:table>


</#if>
</#macro><#-- partslisting -->



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

