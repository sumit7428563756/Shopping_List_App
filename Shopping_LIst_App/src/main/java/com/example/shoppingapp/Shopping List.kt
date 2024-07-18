package com.example.shoppingapp





import android.content.ClipData.Item
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ShoppingList(val id: Int,
                        var item:String,
                        var Quantity:Int,
                        var isediting:Boolean = false)


@Composable
fun ShoppingListApp() {
    var Sitems by remember { mutableStateOf(listOf<ShoppingList>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemname by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )

    {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(32.dp)
        )
        {
            Text("Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
           items(Sitems){
                item ->
               if(item.isediting){
                   Shoppingitemedit(item = item, onEditcomplete = {
                       editedname,editedQuantity ->
                       Sitems = Sitems.map {it.copy(isediting = false)}
                       val editeditem = Sitems.find { it.id == item.id }
                          editeditem?.let {
                              it.item = editedname
                              it.Quantity = editedQuantity
                          }
                   })
               } else{
                   ShoppingListItem(item = item, onEditClick = {
                       Sitems = Sitems.map { it.copy(isediting = it.id == item.id) }
                   }, onDeleteClick ={ Sitems = Sitems - item })
               }
               }
           }
        }

    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog = false},
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                               Button(onClick = {
                                    if(itemname.isNotBlank()){
                                         val newitem = ShoppingList(id = Sitems.size+1,
                                             item = itemname,
                                             Quantity = itemQuantity.toInt())
                                        Sitems = Sitems + newitem
                                        showDialog = false
                                        itemname = ""
                                    }
                               }) {
                                 Text("Add")
                               }
                                Button(onClick = {
                                  showDialog = false
                                }) {
                                    Text("Cancel")
                                }
                            }
            },
            title = { Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemname, onValueChange = {itemname = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                    OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                }

            }
        )
    }
}

@Composable
fun Shoppingitemedit(item: ShoppingList, onEditcomplete: (String, Int) -> Unit){
    var editName by remember{ mutableStateOf(item.item) }
    var editQuantity by remember{ mutableStateOf(item.Quantity.toString()) }
    var isediting by remember{ mutableStateOf(item.isediting) }
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editName,
                onValueChange = { editName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(value = editQuantity, onValueChange = {editQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        Button(onClick = {
            isediting = false
            onEditcomplete(
                editName,
                editQuantity.toIntOrNull() ?: 1
            )
        })
        {
            Text("Save")
        }
    }
}



@Composable
fun ShoppingListItem(
    item: ShoppingList,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.item,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Qty: ${item.Quantity}",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Row(modifier = Modifier.padding(8.dp))
        {
            IconButton(onClick = onEditClick) {
                Row {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
            }
            IconButton(onClick = onDeleteClick)
            {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
