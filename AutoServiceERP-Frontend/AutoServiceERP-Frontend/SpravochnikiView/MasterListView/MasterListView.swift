//
//  MasterListView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 26.11.2023.
//

import SwiftUI

struct MasterListView: View {
    @State private var masters: [Master] = []
    @State private var showingAddMasterView = false

    var body: some View {
        List {
            ForEach(masters, id: \.id) { master in
                NavigationLink(destination: EditMasterView(master: master, onMasterUpdated: fetchMasters)) {
                    Text(master.name)
                }
            }
            .onDelete(perform: deleteMaster)
        }
        .navigationTitle("Мастера")
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                Button(action: fetchMasters) {
                    Text("Обновить")
                }
                Button(action: { showingAddMasterView = true }) {
                    Image(systemName: "plus")
                }
            }
        }
        .sheet(isPresented: $showingAddMasterView, onDismiss: fetchMasters) {
            AddMasterView(masters: $masters)
        }
    }

    private func fetchMasters() {
        NetworkManager.shared.fetchMasters { fetchedMasters in
            self.masters = fetchedMasters
        }
    }
    
    private func deleteMaster(at offsets: IndexSet) {
        offsets.forEach { index in
            let masterId = masters[index].id ?? 0
            NetworkManager.shared.deleteMaster(masterId: masterId) { success in
                if success {
                    DispatchQueue.main.async {
                        masters.remove(at: index)
                    }
                } else {
                    // Обработка ошибок
                }
            }
        }
    }
}


